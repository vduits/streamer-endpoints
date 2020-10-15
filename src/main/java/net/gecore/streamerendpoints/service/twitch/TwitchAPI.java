package net.gecore.streamerendpoints.service.twitch;

import java.net.URL;
import java.util.Map;
import java.util.Optional;
import javax.net.ssl.HttpsURLConnection;
import net.gecore.streamerendpoints.service.twitch.component.ApiReply;
import net.gecore.streamerendpoints.service.twitch.component.RateLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class TwitchAPI {

  private final RateLimitService ratelimitService;

  private final SharedAPI sAPI;

  @Autowired
  public TwitchAPI(RateLimitService ratelimitService, SharedAPI sAPI){
    this.ratelimitService = ratelimitService;
    this.sAPI = sAPI;
  }

  public String request(URL url, HttpMethod httpMethod, Map<String, String> headers)
      throws TwitchAPIException{
    if(ratelimitService.canPerformRequest()){
      HttpsURLConnection con = sAPI.buildConnection(url, httpMethod, headers);
      ApiReply response = sAPI.readResponse(con);
      ratelimitService.updateRateLimit(response.getHeaders());
      return response.getBody();
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
    ApiReply response =  sAPI.readResponse(con);
    return response.getBody();
  }

}
