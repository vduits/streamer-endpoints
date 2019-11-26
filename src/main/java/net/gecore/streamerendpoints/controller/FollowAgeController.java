package net.gecore.streamerendpoints.controller;

import net.gecore.streamerendpoints.service.twitch.FollowAgeService;
import net.gecore.streamerendpoints.service.twitch.TwitchAPI;
import net.gecore.streamerendpoints.service.twitch.TwitchAPIException;
import net.gecore.streamerendpoints.service.utils.FollowAgeUtil;
import net.gecore.streamerendpoints.service.utils.JsonPathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import net.gecore.streamerendpoints.processing.QuoteParser;

@RestController
@RequestMapping("${rest.api}" + "/followage")
public class FollowAgeController {

  private FollowAgeService followAgeService;

  @Autowired
  public FollowAgeController( FollowAgeService followAgeService){
    this.followAgeService = followAgeService;
  }

  //todo one that just grabs from a given integer/id
  @GetMapping(value = "/{followerId}/following/{streamerId}")
  public String retrieveAgeById(@PathVariable String followerId, @PathVariable String streamerId){
    try{
      return this.followAgeService.retrieveFollowAge(followerId, streamerId);
    }catch (TwitchAPIException twe){
      return twe.getMessage();
    }
  }
}
