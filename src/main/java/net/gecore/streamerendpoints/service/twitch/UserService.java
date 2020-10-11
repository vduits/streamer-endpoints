package net.gecore.streamerendpoints.service.twitch;

import java.net.URL;
import java.util.Objects;
import net.gecore.streamerendpoints.configuration.TwitchConfig;
import net.gecore.streamerendpoints.service.twitch.component.URLHelper;
import net.gecore.streamerendpoints.service.twitch.component.UserHelper;
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
  private final TwitchConfig twitchConfig;
  private final AuthService authService;
  private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

  public UserService(TwitchAPI twitchAPI, AuthService authService,
      TwitchConfig twitchConfig) {
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
    var parsedUser = UserHelper.createUserFromTwitchResponse(response);
    if (parsedUser.isPresent()) {
      return parsedUser.get().getUserId();
    } else {
      throw new TwitchAPIException("Could not find user at twitch");
    }
  }

  public String retrieveUserIdFromName(String userName) throws TwitchAPIException {
    URL url = URLHelper.buildUrl(twitchConfig, TwitchEndpoint.users, "?login=" + userName);
    return twitchAPI.request(url, HttpMethod.GET, authService.provideAuthHeaders());
  }

  public String requestFollowAge(long followerId, long streamerId) throws TwitchAPIException {
    String urlBuildUp = "/follows?" + "to_id=" + streamerId + "&" + "from_id=" + followerId;
    URL url = URLHelper.buildUrl(twitchConfig, TwitchEndpoint.users, urlBuildUp);
    String response = twitchAPI.request(url, HttpMethod.GET, authService.provideAuthHeaders());
    return JsonPathUtils.retrieveString(response, "$.data[0].followed_at");
  }

}
