package net.gecore.streamerendpoints.service.igdb;

import net.gecore.streamerendpoints.configuration.TwitchConfig;
import net.gecore.streamerendpoints.service.twitch.AuthService;
import net.gecore.streamerendpoints.service.twitch.TwitchAPIException;
import net.gecore.streamerendpoints.service.utils.JsonPathUtils;
import net.gecore.streamerendpoints.service.igdb.constants.IGDBEndpoint;
import net.gecore.streamerendpoints.service.utils.URLHelper;

import java.net.URL;

public class IGDBGameService {
    private final IGDBAPI igdbapi;
    private final TwitchConfig twitchConfig;
    private final AuthService authService;

    public IGDBGameService(IGDBAPI igdbapi, TwitchConfig twitchConfig, AuthService authService){
        this.igdbapi = igdbapi;
        this.twitchConfig = twitchConfig;
        this.authService = authService;
    }

    public String retrieveGameById(Integer gameId)
            throws TwitchAPIException, IGDBAPIException {
        URL url = URLHelper.buildIGDBUrl(IGDBEndpoint.games);
        String body = "fields url,summary; where id = " + gameId + ";";
        return igdbapi.request(url, authService.provideAuthHeaders(), body);
    }

    public Integer externalGameID(String externalId)
            throws TwitchAPIException, IGDBAPIException {
        URL url = URLHelper.buildIGDBUrl(IGDBEndpoint.external_games);
        String catTwitch = "14";
        String body = String.format("fields game; where category = %s & uid = \"%s\";",catTwitch,externalId);
        String reply = igdbapi.request(url, authService.provideAuthHeaders(), body);
        return JsonPathUtils.retrieveInt(reply, "$[0].game");
    }

}
