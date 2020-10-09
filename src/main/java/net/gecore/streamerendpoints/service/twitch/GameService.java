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
public class GameService {
    private final TwitchAPI twitchAPI;
    private final TwitchConfiguration twitchConfig;
    private final AuthService authService;
    private final Logger LOGGER = LoggerFactory.getLogger(net.gecore.streamerendpoints.service.twitch.StreamService.class);

    public GameService(TwitchAPI twitchAPI, AuthService authService,
                         TwitchConfiguration twitchConfig) {
        this.twitchAPI = twitchAPI;
        this.authService = authService;
        this.twitchConfig = twitchConfig;
    }

    public Game retrieveGameById(long gameId) throws TwitchAPIException {
        URL url = buildUrl(TwitchEndpoint.games, "?id=" + gameId);
        String response = twitchAPI.request(url, HttpMethod.GET, authService.provideAuthHeaders());

        Game game = new Game();
        game.setId(Long.parseLong(JsonPathUtils.retrieveString(response, "$.data[0].id")));
        game.setName(JsonPathUtils.retrieveString(response, "$.data[0].name"));

        return game;
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
