package net.gecore.streamerendpoints.service.igdb;

import net.gecore.streamerendpoints.configuration.TwitchConfig;
import net.gecore.streamerendpoints.service.twitch.AuthService;
import net.gecore.streamerendpoints.service.twitch.TwitchAPIException;

import net.gecore.streamerendpoints.service.utils.JsonPathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

public class IGDBGameService {
    private final IGDBAPI igdbapi;
    private final TwitchConfig twitchConfig;
    private final AuthService authService;
//    private final Logger LOGGER = LoggerFactory.getLogger(IGDBService.class);

    public IGDBGameService(IGDBAPI igdbapi, TwitchConfig twitchConfig, AuthService authService){
        this.igdbapi = igdbapi;
        this.twitchConfig = twitchConfig;
        this.authService = authService;
    }

    public String retrieveGameById(Integer gameId)
            throws IGDBAPIException, TwitchAPIException, MalformedURLException {
        URL url = new URL("https://api.igdb.com/v4/games");
        String body = "fields url,summary; where id = " + gameId + ";";
        return igdbapi.request(url, authService.provideAuthHeaders(), body);
    }

    public Integer externalGameID(String externalId)
            throws MalformedURLException, TwitchAPIException, IGDBAPIException {
        URL url = new URL("https://api.igdb.com/v4/external_games");
        String body = "fields game; where category = 14 & uid = \"" + externalId + "\";";
        String reply = igdbapi.request(url, authService.provideAuthHeaders(), body);
        return JsonPathUtils.retrieveInt(reply, "$[0].game");
    }

}
