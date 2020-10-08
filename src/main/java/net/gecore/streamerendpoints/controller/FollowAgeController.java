package net.gecore.streamerendpoints.controller;

import net.gecore.streamerendpoints.service.twitch.UserService;
import net.gecore.streamerendpoints.service.twitch.TwitchAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${rest.api}" + "/followage")
public class FollowAgeController {

  private UserService userService;

  @Autowired
  public FollowAgeController( UserService userService){
    this.userService = userService;
  }

  //todo check if nightbot source is discord/twitch to allow different parsing.
  @GetMapping(value = "/{firstUser}/following/{secondUser}")
  public String retrieveAgeById(@PathVariable String firstUser, @PathVariable String secondUser,
      @RequestHeader(value = "Nightbot-User", required = false) String nightBotData){
    try{
      return this.userService.retrieveFollowAge(firstUser, secondUser);
    }catch (TwitchAPIException twe){
      return twe.getMessage();
    }
  }
}
