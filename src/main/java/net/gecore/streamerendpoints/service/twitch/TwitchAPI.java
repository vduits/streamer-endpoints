package net.gecore.streamerendpoints.service.twitch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.net.ssl.HttpsURLConnection;
import net.gecore.streamerendpoints.configuration.TwitchConfiguration;
import net.gecore.streamerendpoints.service.twitch.component.RateLimit;
import net.gecore.streamerendpoints.service.twitch.component.RateLimitHelper;
import net.gecore.streamerendpoints.service.twitch.component.RateLimitService;
import net.gecore.streamerendpoints.service.utils.JsonPathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TwitchAPI {

  private TwitchConfiguration twitchConfiguration;
  private static String CLIENTIDHEADER = "Client-ID";
  private static String TWITCHAPI = "https://api.twitch.tv/";
  private static String IDPARAMETER = "?id=";
  private static String LOGINPARAMETER = "?login=";
  private static String FOLLOWSPARAMETER = "/follows?";
  private static String TOID = "to_id=";
  private static String FROMID = "from_id=";

  private static String JPATHLOGINDATA = "$.data[0].id";
  private static String JPATHFOLLOWERDATE = "$.data[0].followed_at";

  private static String RATELIMITREMAININGHEADER = "ratelimit-remaining";

  private Logger LOGGER = LoggerFactory.getLogger(TwitchAPI.class);

  private final RateLimitService ratelimitService;

  @Autowired
  public TwitchAPI(TwitchConfiguration twitchConfiguration, RateLimitService ratelimitService)
      throws TwitchAPIException {
    this.twitchConfiguration = twitchConfiguration;
    this.ratelimitService = ratelimitService;
    performInitialRequest();
  }

  public String request(Endpoint endpoint, String urlParams) throws TwitchAPIException{
    if(ratelimitService.canPerformRequest()){
      HttpsURLConnection con = buildConnection(endpoint, urlParams);
      return readResponse(con);
    }else{
      Optional<RateLimit> rateLimit = ratelimitService.returnRateLimit();
      if(rateLimit.isPresent()){
        throw new TwitchAPIException("ratelimit reached please wait until"+
            rateLimit.get().getResetTime());
      }else{
        throw new TwitchAPIException("ratelimit reached, but unknown. Please wait a minute.");
      }
    }
  }

  private void performInitialRequest() throws TwitchAPIException{
    HttpsURLConnection conn = buildConnection(Endpoint.users, IDPARAMETER +
        twitchConfiguration.getConfiguredUser());
    String response = readResponse(conn);
    if(!response.isEmpty()){
      String userId = JsonPathUtils.retrieveInformation(response, JPATHLOGINDATA);
      LOGGER.info("Did manage to find {}",userId);
    }else{
      LOGGER.error("Did not manage to find user on Twitch with id: {}",
          twitchConfiguration.getConfiguredUser());
      throw new TwitchAPIException("Could not find user at twitch");
    }

  }


  private HttpsURLConnection buildConnection(Endpoint endpoint, String urlParams)
      throws  TwitchAPIException{
    URL url;
    HttpsURLConnection con;
    try {
      url = new URL(
          TWITCHAPI
              + twitchConfiguration.getApiVersion()
              + "/"
              + endpoint.name()
              + urlParams);
      con = (HttpsURLConnection) url.openConnection();
      con.setConnectTimeout(5000);
      con.setReadTimeout(5000);
      con.setRequestMethod("GET");
      con.setRequestProperty(CLIENTIDHEADER, twitchConfiguration.getClientId());
    } catch (MalformedURLException me) {
      throw new TwitchAPIException("Url format is messed up");
    } catch (ProtocolException pe) {
      throw new TwitchAPIException("Sorry connection is complaining");
    } catch (IOException io) {
      throw new TwitchAPIException("Sorry twitch is complaining");
    }
    return con;
  }

  private String readResponse(HttpsURLConnection con){
    StringBuilder content = new StringBuilder();
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
      updateRateLimit(con.getHeaderFields());
      String input;
      while ((input = br.readLine()) != null) {
        content.append(input);
      }
      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    con.disconnect();
    return content.toString();
  }


  public void updateRateLimit(Map<String, List<String>> headers){
    var remaining = headers.get("ratelimit-remaining");
    var resetTime = headers.get("ratelimit-reset");
    Optional<RateLimit> rateLimit = RateLimitHelper.convertToRateLimit(
        remaining.get(0), resetTime.get(0));
    if (rateLimit.isPresent()) {
      ratelimitService.updateRateLimit(rateLimit.get());
      LOGGER.debug(rateLimit.toString());
    }else{
      LOGGER.error("Could not determine ratelimit");
    }
  }

  public String retrieveFollowAge(long followerId, long streamerId) throws TwitchAPIException{
    String urlBuildUp = FOLLOWSPARAMETER+TOID+streamerId+"&"+FROMID+followerId;
    String response = request(Endpoint.users, urlBuildUp);
    return JsonPathUtils.retrieveInformation(response, JPATHFOLLOWERDATE);
  }

  public long retrieveUserIdFromName(String userName) throws TwitchAPIException{
    String response = request(Endpoint.users, LOGINPARAMETER +userName);
    if(Objects.nonNull(response)){
      String userId = JsonPathUtils.retrieveInformation(response, JPATHLOGINDATA);
      return stringToLong(userId);
    }else{
      throw new TwitchAPIException("Could not find user at twitch");
    }
  }

  private long stringToLong(String followerIdString) throws TwitchAPIException{
    try {
      return Long.parseLong(followerIdString);
    } catch (NumberFormatException ne) {
      LOGGER.error("Could not transform '{}' to long", followerIdString);
      throw new TwitchAPIException(ne.getMessage());
    }
  }
}
