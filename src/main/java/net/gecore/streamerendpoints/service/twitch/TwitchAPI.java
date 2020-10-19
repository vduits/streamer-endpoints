package net.gecore.streamerendpoints.service.twitch;

import java.net.URL;
import java.util.Map;
import java.util.Optional;
import javax.net.ssl.HttpsURLConnection;
import net.gecore.streamerendpoints.service.shared.SharedAPI;
import net.gecore.streamerendpoints.service.shared.SharedApiException;
import net.gecore.streamerendpoints.service.shared.component.ApiReply;
import net.gecore.streamerendpoints.service.twitch.component.RateLimit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class TwitchAPI {

  private final RateLimitService ratelimitService;

  private final SharedAPI sAPI;

  private final Logger LOGGER = LoggerFactory.getLogger(TwitchAPI.class);

  @Autowired
  public TwitchAPI(RateLimitService ratelimitService, SharedAPI sAPI) {
    this.ratelimitService = ratelimitService;
    this.sAPI = sAPI;
  }

  public String request(URL url, HttpMethod httpMethod, Map<String, String> headers)
      throws TwitchAPIException {
    if (ratelimitService.canPerformRequest()) {
      try {
        HttpsURLConnection con = sAPI.buildConnection(url, httpMethod, headers);
        ApiReply response = sAPI.readResponse(con);
        ratelimitService.updateRateLimit(response.getHeaders());
        return response.getBody();
      } catch (SharedApiException sae) {
        LOGGER.error(sae.getMessage());
        LOGGER.error("test");
        throw new TwitchAPIException("An error occurred trying to communicate with twitch.");
      }
    } else {
      Optional<RateLimit> rateLimit = ratelimitService.returnRateLimit();
      if (rateLimit.isPresent()) {
        throw new TwitchAPIException("rate limit reached please wait until" +
            rateLimit.get().getResetTime());
      } else {
        throw new TwitchAPIException("rate limit reached, but unknown. Please wait a minute.");
      }
    }
  }

  public String directRequest(URL url, HttpMethod httpMethod, Map<String, String> headers)
      throws SharedApiException {
    HttpsURLConnection con = sAPI.buildConnection(url, httpMethod, headers);
    ApiReply response = sAPI.readResponse(con);
    return response.getBody();
  }

}
