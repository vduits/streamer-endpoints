package net.gecore.streamerendpoints.service.twitch;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
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

    public TwitchGame retrieveCurrentPlayingGameByUserId(long userId) throws TwitchAPIException {
        ReadContext streamContext = JsonPath.parse(retrieveStreamByUserId(userId));
        Optional<Long> gameId = retrieveGameIdFromStream(streamContext);

        if(gameId.isPresent()) {
            return twitchGameService.retrieveGameById(gameId.get());
        }

        throw new TwitchAPIException("The user is currently not streaming.");
    }

    public String retrieveStreamByUserId(long userId) throws TwitchAPIException {
        final String urlParams = "?user_id=" + userId;
        URL url = URLHelper.buildUrl(twitchConfig, TwitchEndpoint.streams, urlParams);

        return twitchAPI.request(url, HttpMethod.GET, authService.provideAuthHeaders());
    }

    /**
     * Extract Game Id from current stream context
     *
     * @param streamContext Current stream
     * @return Optional Game Id, Could be empty if the user is not streaming
     */
    private Optional<Long> retrieveGameIdFromStream(ReadContext streamContext) {
        final String node = "$.data[0].game_id";

        if(isUserStreaming(streamContext)) {
            return Optional.of(Long.parseLong(streamContext.read(node)));
        }

        return Optional.empty();
    }

    /**
     * Checking Twitch API Response for filled data array
     *
     * @param streamContext Twitch Api Response from /streams api point
     * @return boolean
     */
    private boolean isUserStreaming(ReadContext streamContext)  {
        JSONArray arr = streamContext.read("$.data");
        return !arr.isEmpty();
    }

}
