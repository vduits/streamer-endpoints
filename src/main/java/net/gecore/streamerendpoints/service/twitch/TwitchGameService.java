package net.gecore.streamerendpoints.service.twitch;

import java.util.Optional;
import net.gecore.streamerendpoints.configuration.TwitchConfig;
import net.gecore.streamerendpoints.domain.TwitchGame;
import net.gecore.streamerendpoints.service.twitch.component.GameHelper;
import net.gecore.streamerendpoints.service.utils.URLHelper;
import net.gecore.streamerendpoints.service.twitch.constants.TwitchEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
public class TwitchGameService {
    private final TwitchAPI twitchAPI;
    private final TwitchConfig twitchConfig;
    private final AuthService authService;
    private final Logger LOGGER = LoggerFactory.getLogger(StreamService.class);

    public TwitchGameService(TwitchAPI twitchAPI, AuthService authService, TwitchConfig twitchConfig) {
        this.twitchAPI = twitchAPI;
        this.authService = authService;
        this.twitchConfig = twitchConfig;
    }

    public TwitchGame retrieveGameById(long gameId) throws TwitchAPIException {
        URL url = URLHelper.buildTwitchUrl(twitchConfig, TwitchEndpoint.games, "?id=" + gameId);
        String response = twitchAPI.request(url, HttpMethod.GET, authService.provideAuthHeaders());
        Optional<TwitchGame> parsedGame = GameHelper.createGameFromTwitchResponse(response);
        if(parsedGame.isPresent()){
            return parsedGame.get();
        }else{
            LOGGER.error("Could not find game at twitch using id: {}", gameId);
            throw new TwitchAPIException("Could not find the game the streamer is playing");
        }
    }

}
