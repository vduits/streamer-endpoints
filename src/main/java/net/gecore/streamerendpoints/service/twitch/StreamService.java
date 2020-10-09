package net.gecore.streamerendpoints.service.twitch;

import net.gecore.streamerendpoints.configuration.TwitchConfiguration;
import net.gecore.streamerendpoints.domain.Game;
import net.gecore.streamerendpoints.service.twitch.constants.TwitchEndpoint;
import net.gecore.streamerendpoints.service.utils.JsonPathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;

@Component
public class StreamService {

    private final TwitchAPI twitchAPI;
    private final TwitchConfiguration twitchConfig;
    private final AuthService authService;
    private final GameService gameService;
    private final Logger LOGGER = LoggerFactory.getLogger(net.gecore.streamerendpoints.service.twitch.StreamService.class);

    public StreamService(TwitchAPI twitchAPI, AuthService authService,
                        GameService gameService, TwitchConfiguration twitchConfig) {
        this.twitchAPI = twitchAPI;
        this.authService = authService;
        this.gameService = gameService;
        this.twitchConfig = twitchConfig;
    }

    public Game retrieveCurrentPlayingGameByUserId(long userId) throws TwitchAPIException {
        final String urlParams = "?user_id=" + userId;
        final String jsonNode = "$.data[0].game_id";

        URL url = buildUrl(TwitchEndpoint.streams, urlParams);
        String response = twitchAPI.request(url, HttpMethod.GET, authService.provideAuthHeaders());
        long gameId = Long.parseLong(JsonPathUtils.retrieveString(response, jsonNode));

        return gameService.retrieveGameById(gameId);
    }

    // @TODO: duplicate method - move to Utils or TwitchApi Class
    public URL buildUrl(TwitchEndpoint twitchEndpoint, String urlParams) throws TwitchAPIException {
        try {
            return new URL(
                    "https://api.twitch.tv/"
                            + twitchConfig.getApiVersion()
                            + "/"
                            + twitchEndpoint.name()
                            + urlParams);
        } catch (MalformedURLException me) {
            LOGGER.error("Encountered an issue trying to build the url: " + me.getMessage());
            throw new TwitchAPIException("There is configuration issue contacting the twitch api");
        }
    }
}
