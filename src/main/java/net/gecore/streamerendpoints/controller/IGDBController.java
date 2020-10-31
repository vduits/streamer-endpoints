package net.gecore.streamerendpoints.controller;

import java.util.List;

import net.gecore.streamerendpoints.domain.TwitchGame;
import net.gecore.streamerendpoints.service.igdb.IGDBAPIException;
import net.gecore.streamerendpoints.service.igdb.IGDBGameService;
import net.gecore.streamerendpoints.service.twitch.StreamService;
import net.gecore.streamerendpoints.service.twitch.TwitchAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("${rest.api}" + "/user")
public class IGDBController {

    private final IGDBGameService igdbGameService;
    private final StreamService streamService;

    @Autowired
    public IGDBController(IGDBGameService igdbGameService, StreamService streamService){
        this.igdbGameService = igdbGameService;
        this.streamService = streamService;
    }

    @GetMapping(value = "/{userId}/stream/game")
    public String retrieveGameInfo(@PathVariable Integer userId,
        @RequestParam(required = false) List<String> filters)
            throws TwitchAPIException, IGDBAPIException {

        TwitchGame twitchGame = streamService.retrieveCurrentPlayingGameByUserId(userId);
        String twitchGameString = Integer.toString((int) twitchGame.getId());
        Integer exReply = igdbGameService.retrieveIdByExternalGame(twitchGameString);
        return igdbGameService.retrieveGameById(exReply);
    }

}
