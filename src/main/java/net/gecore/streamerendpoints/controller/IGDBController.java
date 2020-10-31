package net.gecore.streamerendpoints.controller;

import java.util.List;

import net.gecore.streamerendpoints.service.igdb.IGDBGameService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${rest.api}" + "/user/{userId}/stream/game")
public class IGDBController {

    private final IGDBGameService igdbGameService;

    public IGDBController(IGDBGameService igdbGameService){
        this.igdbGameService = igdbGameService;
    }

    @GetMapping(value = "/{userId}/stream/game")
    public void retrieveGameInfo(@PathVariable Integer userId,
                                 @RequestParam(required = false) List<String> filters){

    }

}
