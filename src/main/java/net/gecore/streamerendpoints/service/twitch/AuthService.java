package net.gecore.streamerendpoints.service.twitch;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.gecore.streamerendpoints.configuration.TwitchConfig;
import net.gecore.streamerendpoints.service.twitch.component.TwitchToken;
import net.gecore.streamerendpoints.service.twitch.constants.GrantType;
import net.gecore.streamerendpoints.service.twitch.constants.OAuthEndpoint;
import net.gecore.streamerendpoints.service.utils.JsonPathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class AuthService {

  private static final String OAUTH_TWITCH = "https://id.twitch.tv/oauth2/";
  private final TwitchConfig configuration;
  private static final String URL_ERROR = "The server has trouble learning how to write urls";
  private static final Map<String, String> NO_HEADERS = new HashMap<>();
  private final TwitchAPI twitchAPI;
  private TwitchToken twitchToken;

  private final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

  public AuthService(TwitchAPI twitchAPI, TwitchConfig configuration) {
    this.twitchAPI = twitchAPI;
    this.configuration = configuration;
  }

  public Map<String, String> provideAuthHeaders() throws TwitchAPIException {
    if (checkIfTokenExpired()) {
      retrieveAuthToken();
    }
    var map = new HashMap<String, String>();
    map.put("Client-ID", configuration.getClientId());
    map.put("Authorization", "Bearer " + twitchToken.getToken());
    return map;
  }

  public boolean checkIfTokenExpired() {
    if (Objects.isNull(this.twitchToken)) {
      return true;
    } else {
      var now = LocalDateTime.now();
      var prediction = this.twitchToken.getExpiresAt().minus(1000, ChronoUnit.SECONDS);
      return now.isAfter(prediction);
    }
  }

  public void retrieveAuthToken() throws TwitchAPIException {
    URL url = createAuthRequestUrl(createAuthParams());
    String response = twitchAPI.directRequest(url, HttpMethod.POST, NO_HEADERS);
    String token = JsonPathUtils.retrieveString(response, "$.access_token");
    int expiry = JsonPathUtils.retrieveInt(response, "$.expires_in");
    this.twitchToken = new TwitchToken(token, expiry);
  }

  private String createAuthParams() {
    return String.format(
        "?client_id=%s"
            + "&client_secret=%s"
            + "&grant_type=%s",
        configuration.getClientId(),
        configuration.getClientSecret(),
        GrantType.client_credentials
    );
  }

  private URL createAuthRequestUrl(String urlParams) throws TwitchAPIException {
    try {
      return new URL(OAUTH_TWITCH + OAuthEndpoint.token + urlParams);
    } catch (MalformedURLException me) {
      LOGGER.error(me.getMessage());
      throw new TwitchAPIException(URL_ERROR);
    }
  }

  public boolean validateToken() throws TwitchAPIException {
    var headers = new HashMap<String, String>();
    headers.put("Authorization", "OAuth " + twitchToken.getToken());
    var result = twitchAPI.directRequest(createValidateUrl(), HttpMethod.GET, headers);
    return false;
  }

  private URL createValidateUrl() throws TwitchAPIException {
    try {
      return new URL(OAUTH_TWITCH + OAuthEndpoint.validate);
    } catch (MalformedURLException me) {
      LOGGER.error(me.getMessage());
      throw new TwitchAPIException(URL_ERROR);
    }
  }

}
