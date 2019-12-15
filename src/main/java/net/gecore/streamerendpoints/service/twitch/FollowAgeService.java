package net.gecore.streamerendpoints.service.twitch;

import net.gecore.streamerendpoints.configuration.TwitchConfiguration;
import net.gecore.streamerendpoints.service.utils.FollowAgeUtil;
import net.gecore.streamerendpoints.service.utils.JsonPathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FollowAgeService {

  private final String CONFIGUREDUSERSTRING;
  private final long CONFIGUREDUSER;

  private TwitchAPI twitchAPI;
  private Logger LOGGER = LoggerFactory.getLogger(FollowAgeService.class);

  public FollowAgeService(TwitchAPI twitchAPI, TwitchConfiguration twitchConfiguration) {
    this.twitchAPI = twitchAPI;
    CONFIGUREDUSER = twitchConfiguration.getConfiguredUser();
    CONFIGUREDUSERSTRING = String.valueOf(CONFIGUREDUSER);
  }

  public String retrieveFollowAge(String firstUser, String secondUser) throws TwitchAPIException {
    long firstUserId = retrieveUserFromString(firstUser);
    long secondUserId = retrieveUserFromString(secondUser);
    String fetchedDate = twitchAPI.retrieveFollowAge(firstUserId, secondUserId);
    if (!fetchedDate.equals(JsonPathUtils.badResult)) {
      return FollowAgeUtil.calculate(fetchedDate);
    }
    return "Sorry can't help you";
  }

  /**
   * Checks if the user is the same as the configured user and returns the correct Twitch User Id.
   * This saves an additional twitch API call if it matches the configured user.
   *
   * @param user String containing name of user;
   * @return long twitch id of user;
   */
  public long retrieveUserFromString(String user) throws TwitchAPIException {
    if (user.equals(CONFIGUREDUSERSTRING)) {
      return CONFIGUREDUSER;
    } else {
      return twitchAPI.retrieveUserIdFromName(user);
    }
  }

}
