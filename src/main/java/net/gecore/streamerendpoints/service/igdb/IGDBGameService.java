package net.gecore.streamerendpoints.service.igdb;

import java.net.URL;
import net.gecore.streamerendpoints.service.igdb.constants.ExternalGameCategory;
import net.gecore.streamerendpoints.service.igdb.constants.ExternalGameField;
import net.gecore.streamerendpoints.service.igdb.constants.GamesField;
import net.gecore.streamerendpoints.service.igdb.constants.IGDBEndpoint;
import net.gecore.streamerendpoints.service.igdb.helper.IGDBQuery;
import net.gecore.streamerendpoints.service.utils.JsonPathUtils;
import net.gecore.streamerendpoints.service.utils.URLHelper;

public class IGDBGameService {

  private final IGDBAPI igdbapi;

  public IGDBGameService(IGDBAPI igdbapi) {
    this.igdbapi = igdbapi;
  }

  public String retrieveGameById(Integer gameId) throws IGDBAPIException {
    URL url = URLHelper.buildIGDBUrl(IGDBEndpoint.games);
    IGDBQuery query = new IGDBQuery()
        .fields(GamesField.url, GamesField.summary)
        .where(GamesField.id, Integer.toString(gameId));
    return igdbapi.request(url, query.build());
  }

  public int retrieveIdByExternalGame(String externalId) throws IGDBAPIException {
    URL url = URLHelper.buildIGDBUrl(IGDBEndpoint.external_games);
    int twitchCategory = ExternalGameCategory.TWITCH.getValue();
    IGDBQuery query = new IGDBQuery()
        .fields(ExternalGameField.game)
        .where(ExternalGameField.category, twitchCategory)
        .and().where(ExternalGameField.uid, externalId);
    String result = igdbapi.request(url, query.build());
    return JsonPathUtils.retrieveInt(result, "$[0]." + ExternalGameField.game);

  }

}
