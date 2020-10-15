package net.gecore.streamerendpoints.service.twitch;

import java.net.URL;
import java.util.Map;
import java.util.Optional;
import javax.net.ssl.HttpsURLConnection;
import net.gecore.streamerendpoints.service.twitch.SharedAPI;
import net.gecore.streamerendpoints.service.twitch.component.RateLimit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class TwitchAPI {

  private final RateLimitService ratelimitService;

//  private final Logger LOGGER = LoggerFactory.getLogger(TwitchAPI.class);

  private final SharedAPI sAPI;

  @Autowired
  public TwitchAPI(RateLimitService ratelimitService){
    this.ratelimitService = ratelimitService;
    sAPI = new SharedAPI();
  }

  public String request(URL url, HttpMethod httpMethod, Map<String, String> headers)
      throws TwitchAPIException{
    if(ratelimitService.canPerformRequest()){
      HttpsURLConnection con = sAPI.buildConnection(url, httpMethod, headers);
      return sAPI.readResponse(con, true);
    }else{
      Optional<RateLimit> rateLimit = ratelimitService.returnRateLimit();
      if(rateLimit.isPresent()){
        throw new TwitchAPIException("rate limit reached please wait until"+
            rateLimit.get().getResetTime());
      }else{
        throw new TwitchAPIException("rate limit reached, but unknown. Please wait a minute.");
      }
    }
  }

  public String directRequest(URL url, HttpMethod httpMethod, Map<String, String> headers)
      throws TwitchAPIException {
    HttpsURLConnection con = sAPI.buildConnection(url, httpMethod, headers);
    return sAPI.readResponse(con, false);
  }

}
