package net.gecore.streamerendpoints.service.rest;

import net.gecore.streamerendpoints.service.twitch.TwitchAPI;
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

  private QuoteParser quoteParser;
  private TwitchAPI twitchAPI;
  private static final String IZOIDSTRING = "44566682";
  private static final int IZOID = 44566682;
  private static final String NOPE = "NOPE";

  @Autowired
  public FollowAgeController( QuoteParser quoteParser, TwitchAPI twitchAPI){
    this.quoteParser = quoteParser;
    this.twitchAPI = twitchAPI;
  }



  //todo one that just grabs from a given integer/id
  @GetMapping(value = "/{followerId}/following/{streamerId}")
  public String retrieveAgeById(@PathVariable String followerId, @PathVariable String streamerId){
    String followAge = "Sorry can't help you";

    String followerIdString = twitchAPI.retrieveUserFromName(followerId);
    int followerIdInteger = 0;
    try{
      followerIdInteger = Integer.valueOf(followerIdString) ;
    }catch (NumberFormatException ne){
      System.out.println(ne.getMessage());
    }
    String resultedDate = NOPE;
    if(followerIdInteger != 0){
      if(streamerId.equals(IZOIDSTRING)){
        resultedDate = twitchAPI.retrieveFollowAge(followerIdInteger,IZOID);
      }else{
        int streamerIdInteger;
        try{
          streamerIdInteger = Integer.valueOf(twitchAPI.retrieveUserFromName(streamerId));
        }catch (NumberFormatException ne){
          System.out.println(ne.getMessage());
          return "Cannot find the streamer there";
        }
        resultedDate = twitchAPI.retrieveFollowAge(followerIdInteger,streamerIdInteger);
      }
    }else{
      return "Sorry can't find the user following";
    }

    //cheapo check
    if(!resultedDate.equals(JsonPathUtils.badResult) && !resultedDate.equals(NOPE)){
      return FollowAgeUtil.calculate(resultedDate);
    }

    return followAge;
  }




  //todo one that grabs a string and checks the twitch api.
}
