package net.gecore.streamerendpoints.service.twitch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import javax.net.ssl.HttpsURLConnection;
import net.gecore.streamerendpoints.service.twitch.component.RateLimit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class TwitchAPI {

  private final RateLimitService ratelimitService;

  private final Logger LOGGER = LoggerFactory.getLogger(TwitchAPI.class);

  @Autowired
  public TwitchAPI(RateLimitService ratelimitService){
    this.ratelimitService = ratelimitService;
  }

  public String request(URL url, HttpMethod httpMethod, Map<String, String> headers)
      throws TwitchAPIException{
    if(ratelimitService.canPerformRequest()){
      HttpsURLConnection con = buildConnection(url, httpMethod, headers);
      return readResponse(con, true);
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
    HttpsURLConnection con = buildConnection(url, httpMethod, headers);
    return readResponse(con, false);
  }

  private HttpsURLConnection buildConnection(URL url, HttpMethod httpMethod,
      Map<String, String> headers) throws  TwitchAPIException{
    HttpsURLConnection conn;
    try {
      conn = (HttpsURLConnection) url.openConnection();
      conn.setConnectTimeout(5000);
      conn.setReadTimeout(5000);
      conn.setRequestMethod(httpMethod.name());
      if(!headers.isEmpty()){
        addHeaders(conn, headers);
      }
    } catch (ProtocolException pe) {
      LOGGER.error(pe.getMessage());
      throw new TwitchAPIException("Protocol error encountered");
    } catch (IOException io) {
      LOGGER.error(io.getMessage());
      throw new TwitchAPIException("Connection with twitch got interrupted");
    }
    return conn;
  }

  private void addHeaders(HttpsURLConnection conn, Map<String, String> headers){
    for(Map.Entry<String,String> entry : headers.entrySet()){
      conn.setRequestProperty(entry.getKey(), entry.getValue());
    }
  }

  private String readResponse(HttpsURLConnection con, boolean updateRateLimit){
    StringBuilder content = new StringBuilder();
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
      if(updateRateLimit){
        ratelimitService.updateRateLimit(con.getHeaderFields());
      }
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


}
