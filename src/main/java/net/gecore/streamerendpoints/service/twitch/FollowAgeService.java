package net.gecore.streamerendpoints.service.twitch;

import net.gecore.streamerendpoints.service.utils.FollowAgeUtil;
import net.gecore.streamerendpoints.service.utils.JsonPathUtils;
import org.springframework.stereotype.Component;

@Component
public class FollowAgeService {

  private static final String IZOIDSTRING = "44566682";
  private static final int IZOID = 44566682;
  private static final String NOPE = "NOPE";

  private TwitchAPI twitchAPI;

  public FollowAgeService(TwitchAPI twitchAPI){
    this.twitchAPI = twitchAPI;
  }
  //todo add streamer configuration to be able to remove izoid

  public String retrieveFollowAge(String followerId, String streamerId) throws TwitchAPIException{
    String followAge = "Sorry can't help you";
    String followerIdString = twitchAPI.retrieveUserFromName(followerId);
    int followerIdInteger = stringToInt(followerIdString);
    String resultedDate = NOPE;
    if(followerIdInteger != 0){
      if(streamerId.equals(IZOIDSTRING)){
        resultedDate = twitchAPI.retrieveFollowAge(followerIdInteger,IZOID);
      }else{
        int streamerIdInteger;
        try{
          streamerIdInteger = Integer.parseInt(twitchAPI.retrieveUserFromName(streamerId));
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

  private int stringToInt(String followerIdString){
    try{
      return Integer.parseInt(followerIdString) ;
    }catch (NumberFormatException ne){
      System.out.println(ne.getMessage());
    }
    return 0;
  }



}
