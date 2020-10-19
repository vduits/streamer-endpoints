package net.gecore.streamerendpoints.service.twitch.component;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.ReadContext;
import java.util.Optional;
import net.gecore.streamerendpoints.domain.TwitchStream;

public class StreamHelper {

  public static Optional<TwitchStream> createStreamFromTwitchResponse(String response) {
    ReadContext ctx = JsonPath.parse(response);
    var stream = new TwitchStream();
    try {
      stream.setGameId(Long.parseLong(ctx.read("$.data[0].game_id")));
      stream.setTitle(ctx.read("$.data[0].title"));
      stream.setTagIds(ctx.read("$.data[0].tag_ids.*"));
      return Optional.of(stream);
    } catch (PathNotFoundException pnf) {
      return Optional.empty();
    }
  }

}
