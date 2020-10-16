package net.gecore.streamerendpoints.service.twitch;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import net.gecore.streamerendpoints.configuration.TwitchConfig;
import net.gecore.streamerendpoints.domain.TwitchGame;
import net.gecore.streamerendpoints.service.twitch.component.URLHelper;
import net.gecore.streamerendpoints.service.twitch.constants.TwitchEndpoint;
import net.gecore.streamerendpoints.service.utils.JsonPathUtils;
import net.minidev.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Optional;

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

    public Optional<TwitchGame> retrieveCurrentPlayingGameByUserId(long userId) throws TwitchAPIException {
        final String jsonNode = "$.data[0].game_id";

        Optional<String> streamResponse = retrieveStreamByUserId(userId);

        if(streamResponse.isPresent()) {
            long gameId = Long.parseLong(JsonPathUtils.retrieveString(streamResponse.get(), jsonNode));
            return Optional.of(twitchGameService.retrieveGameById(gameId));
        }

        return Optional.empty();
    }

    public Optional<String> retrieveStreamByUserId(long userId) throws TwitchAPIException {
        final String urlParams = "?user_id=" + userId;
        URL url = URLHelper.buildUrl(twitchConfig, TwitchEndpoint.streams, urlParams);
        String streamResponse = twitchAPI.request(url, HttpMethod.GET, authService.provideAuthHeaders());

        if(!isUserStreaming(streamResponse)) {
            throw new TwitchAPIException("User not exists or not streaming at the moment.");
        }

        return Optional.of(streamResponse);
    }

    /**
     * Checking Twitch API Response for filled data array
     *
     * @param streamResponse Twitch Api Response from /streams api point
     * @return boolean
     */
    private boolean isUserStreaming(String streamResponse)  {
        ReadContext context = JsonPath.parse(streamResponse);
        JSONArray arr = context.read("$.data");
        return !arr.isEmpty();
    }

}
