package net.gecore.streamerendpoints.service.twitch;

import net.gecore.streamerendpoints.configuration.TwitchConfig;
import net.gecore.streamerendpoints.domain.TwitchGame;
import net.gecore.streamerendpoints.service.twitch.component.URLHelper;
import net.gecore.streamerendpoints.service.twitch.constants.TwitchEndpoint;
import net.gecore.streamerendpoints.service.utils.JsonPathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
public class StreamService {

    private final TwitchAPI twitchAPI;
    private final TwitchConfig twitchConfig;
    private final AuthService authService;
    private final TwitchGameService twitchGameService;
    private final Logger LOGGER = LoggerFactory.getLogger(StreamService.class);

    public StreamService(TwitchAPI twitchAPI, AuthService authService,
                        TwitchGameService twitchGameService, TwitchConfig twitchConfig) {
        this.twitchAPI = twitchAPI;
        this.authService = authService;
        this.twitchGameService = twitchGameService;
        this.twitchConfig = twitchConfig;
    }

    public TwitchGame retrieveCurrentPlayingGameByUserId(long userId) throws TwitchAPIException {
        var response = retrieveStreamByUserId(userId);
        final String jsonNode = "$.data[0].game_id";
        long gameId = Long.parseLong(JsonPathUtils.retrieveString(response, jsonNode));
        return twitchGameService.retrieveGameById(gameId);
    }

    public String retrieveStreamByUserId(long userId) throws TwitchAPIException {
        final String urlParams = "?user_id=" + userId;
        URL url = URLHelper.buildUrl(twitchConfig, TwitchEndpoint.streams, urlParams);
        return twitchAPI.request(url, HttpMethod.GET, authService.provideAuthHeaders());
    }

}
