package net.gecore.streamerendpoints.service.igdb;

import java.net.URL;
import net.gecore.streamerendpoints.service.igdb.constants.GamesFields;
import net.gecore.streamerendpoints.service.igdb.constants.IGDBEndpoint;
import net.gecore.streamerendpoints.service.utils.JsonPathUtils;
import net.gecore.streamerendpoints.service.utils.URLHelper;

public class IGDBGameService {

  private final IGDBAPI igdbapi;

  public IGDBGameService(IGDBAPI igdbapi) {
    this.igdbapi = igdbapi;
  }

  public String retrieveGameById(Integer gameId) throws IGDBAPIException {
    URL url = URLHelper.buildIGDBUrl(IGDBEndpoint.games);
    String body = String.format("fields %s,%s; where id = %s;",
        GamesFields.url, GamesFields.summary, gameId);
    return igdbapi.request(url, body);
  }

  public Integer retrieveIdByExternalGame(String externalId) throws IGDBAPIException {
    URL url = URLHelper.buildIGDBUrl(IGDBEndpoint.external_games);
    String twitchCategory = "14";
    String body = String.format(
        "fields game; where category = %s & uid = \"%s\";", twitchCategory, externalId);
    String reply = igdbapi.request(url, body);
    return JsonPathUtils.retrieveInt(reply, "$[0].game");
  }

}
