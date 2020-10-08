package net.gecore.streamerendpoints.service.twitch;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import net.gecore.streamerendpoints.configuration.TwitchConfiguration;
import net.gecore.streamerendpoints.service.twitch.constants.TwitchEndpoint;
import net.gecore.streamerendpoints.service.utils.BasicUtils;
import net.gecore.streamerendpoints.service.utils.FollowAgeUtil;
import net.gecore.streamerendpoints.service.utils.JsonPathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class UserService {

  private final TwitchAPI twitchAPI;
  private final TwitchConfiguration twitchConfig;
  private final AuthService authService;
  private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
  private static final boolean INCLUDE_AUTH = true;

  public UserService(TwitchAPI twitchAPI, AuthService authService,
      TwitchConfiguration twitchConfig) {
    this.twitchAPI = twitchAPI;
    this.authService = authService;
    this.twitchConfig = twitchConfig;
  }

  public String retrieveFollowAge(String firstUser, String secondUser) throws TwitchAPIException {
    long firstUserId = retrieveUserFromString(firstUser);
    long secondUserId = retrieveUserFromString(secondUser);
    String fetchedDate = requestFollowAge(firstUserId, secondUserId);
    if (!fetchedDate.equals(JsonPathUtils.badResult)) {
      return FollowAgeUtil.calculate(fetchedDate);
    }
    return "Sorry can't help you";
  }

  public long retrieveUserFromString(String user) throws TwitchAPIException {
    var response = retrieveUserIdFromName(user);
    if (Objects.nonNull(response)) {
      String userId = JsonPathUtils.retrieveString(response, "$.data[0].id");
      return BasicUtils.stringToLong(userId);
    } else {
      throw new TwitchAPIException("Could not find user at twitch");
    }
  }

  public String retrieveUserIdFromName(String userName) throws TwitchAPIException {
    URL url = buildUrl(TwitchEndpoint.users, "?login=" + userName);
    return twitchAPI.request(url, HttpMethod.GET, authService.provideAuthHeaders());
  }

  public String requestFollowAge(long followerId, long streamerId) throws TwitchAPIException {
    String urlBuildUp = "/follows?" + "to_id=" + streamerId + "&" + "from_id=" + followerId;
    URL url = buildUrl(TwitchEndpoint.users, urlBuildUp);
    String response = twitchAPI.request(url, HttpMethod.GET, authService.provideAuthHeaders());
    return JsonPathUtils.retrieveString(response, "$.data[0].followed_at");
  }



  public URL buildUrl(TwitchEndpoint twitchEndpoint, String urlParams) throws TwitchAPIException {
    try {
      return new URL(
          "https://api.twitch.tv/"
              + twitchConfig.getApiVersion()
              + "/"
              + twitchEndpoint.name()
              + urlParams);
    } catch (MalformedURLException me) {
      LOGGER.error("Encountered an issue trying to build the url: " + me.getMessage());
      throw new TwitchAPIException("There is configuration issue contacting the twitch api");
    }
  }

  //    ("Client-ID", twitchConfiguration.getClientId());
  //    ("Authorization", "Bearer "+ "");


}
