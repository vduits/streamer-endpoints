package net.gecore.streamerendpoints.service.twitch.component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import net.gecore.streamerendpoints.domain.TwitchGame;
import org.junit.jupiter.api.Test;

public class TwitchGameHelperTest {


  @Test
  void createGameFromTwitchResponse_validResponse_(){
    // Arrange
    long gameId = 33214;
    String gameName = "Fornite";
    String twitchResponse =  ""
        + "{\n"
        + "    \"data\": [{\n"
        + "            \"id\": \""+gameId+"\",\n"
        + "            \"name\": \""+gameName+"\"\n"
        + "        }\n"
        + "    ]\n"
        + "}\n";

    // Act
    var result = GameHelper.createGameFromTwitchResponse(twitchResponse);

    // Assert
    if(result.isPresent()){
      TwitchGame twitchGame = result.get();
      assertEquals(gameId, twitchGame.getId());
      assertEquals(gameName, twitchGame.getName());
    }else{
      fail();
    }

  }

  @Test
  void createGameFromTwitchResponse_noData_fail(){
    // Arrange
    String twitchResponse =  "{\"data\":[],\"pagination\":{}}";

    // Act
    var result = GameHelper.createGameFromTwitchResponse(twitchResponse);

    // Assert
    assertTrue(result.isEmpty());
  }

}
