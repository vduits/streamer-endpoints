package net.gecore.streamerendpoints.service.twitch.component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import net.gecore.streamerendpoints.domain.TwitchStream;
import org.junit.jupiter.api.Test;

public class StreamHelperTest {

  private String replaceTagClutter(String tags){
    return tags.replaceAll("\\[?\"?\",*]?", "");
  }

  @Test
  void createGameFromTwitchResponse_validResponseSingleTag_succeed() {
    // Arrange
    long gameId = 33214;
    String streamTitle = "Streaming Title";
    String tagIds = "[\"6ea6bca4-4712-4ab9-a906-e3336a9d8039\"]";
    String twitchResponse = ""
        + "{\n"
        + "    \"data\": [{\n"
        + "            \"game_id\": \"" + gameId + "\",\n"
        + "            \"title\": \"" + streamTitle + "\",\n"
        + "            \"tag_ids\": "+ tagIds + ",\n"
        + "        }\n"
        + "    ]\n"
        + "}\n";
    // Act
    var result = StreamHelper.createStreamFromTwitchResponse(twitchResponse);

    // Assert
    if (result.isPresent()) {
      TwitchStream twitchStream = result.get();
      assertEquals(gameId, twitchStream.getGameId());
      assertEquals(streamTitle, twitchStream.getTitle());
      String tags = replaceTagClutter(tagIds);
      assertEquals(tags, twitchStream.getTagIds().get(0));
    } else {
      fail();
    }

  }

  @Test
  void createGameFromTwitchResponse_validResponseMultiTag_succeed() {
    // Arrange
    long gameId = 33214;
    String streamTitle = "Streaming Title";
    String firstTag = "6ea6bca4-4712-4ab9-a906-e3336a9d8039";
    String secondTag = "523542352352-esfsa-ef-4353";
    String tagIds = "[\""+firstTag+"\", \""+secondTag+"\"]";
    String twitchResponse = ""
        + "{\n"
        + "    \"data\": [{\n"
        + "            \"game_id\": \"" + gameId + "\",\n"
        + "            \"title\": \"" + streamTitle + "\",\n"
        + "            \"tag_ids\": "+ tagIds + ",\n"
        + "        }\n"
        + "    ]\n"
        + "}\n";
    // Act
    var result = StreamHelper.createStreamFromTwitchResponse(twitchResponse);

    // Assert
    if (result.isPresent()) {
      TwitchStream twitchStream = result.get();
      assertEquals(gameId, twitchStream.getGameId());
      assertEquals(streamTitle, twitchStream.getTitle());
      String tags = replaceTagClutter(tagIds);
      assertEquals(firstTag, twitchStream.getTagIds().get(0));
      assertEquals(secondTag, twitchStream.getTagIds().get(1));
    } else {
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
