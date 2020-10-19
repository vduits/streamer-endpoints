package net.gecore.streamerendpoints.service.twitch.component;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.ReadContext;
import java.util.Optional;
import net.gecore.streamerendpoints.domain.TwitchGame;

public class GameHelper {


  public static Optional<TwitchGame> createGameFromTwitchResponse(String response) {
    ReadContext ctx = JsonPath.parse(response);
    var game = new TwitchGame();
    try {
      game.setId(Long.parseLong(ctx.read("$.data[0].id")));
      game.setName(ctx.read("$.data[0].name"));
      return Optional.of(game);
    } catch (PathNotFoundException pnf) {
      return Optional.empty();
    }
  }

}
