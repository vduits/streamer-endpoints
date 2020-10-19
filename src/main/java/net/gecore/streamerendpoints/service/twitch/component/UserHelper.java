package net.gecore.streamerendpoints.service.twitch.component;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.ReadContext;
import java.util.Optional;
import net.gecore.streamerendpoints.domain.TwitchUser;

public class UserHelper {

  public static Optional<TwitchUser> createUserFromTwitchResponse(String response) {
    ReadContext ctx = JsonPath.parse(response);
    var user = new TwitchUser();
    try {
      user.setUserId(Long.parseLong(ctx.read("$.data[0].id")));
      user.setLogin(ctx.read("$.data[0].login"));
      user.setDisplayName(ctx.read("$.data[0].display_name"));
      return Optional.of(user);
    } catch (PathNotFoundException pnf) {
      return Optional.empty();
    }

  }

}
