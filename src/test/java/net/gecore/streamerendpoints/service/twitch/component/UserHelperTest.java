package net.gecore.streamerendpoints.service.twitch.component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import net.gecore.streamerendpoints.domain.TwitchUser;
import org.junit.jupiter.api.Test;

public class UserHelperTest {

  @Test
  void createUserFromTwitchResponse_validResponse_succeed(){
    // Arrange
    long userId = 12345678;
    String userLower = "user_name";
    String userUpper = "User_Name";
    String twitchResponse =  ""
        + "{\n"
        + "    \"data\": [{\n"
        + "            \"id\": \""+userId+"\",\n"
        + "            \"login\": \""+userLower+"\",\n"
        + "            \"display_name\": \""+userUpper+"\",\n"
        + "        }\n"
        + "    ]\n"
        + "}\n";

    // Act
    var result = UserHelper.createUserFromTwitchResponse(twitchResponse);

    // Assert
    if(result.isPresent()){
      TwitchUser user = result.get();
      assertEquals(userId, user.getUserId());
      assertEquals(userLower, user.getLogin());
      assertEquals(userUpper, user.getDisplayName());
    }else{
      fail();
    }

  }

  @Test
  void createUserFromTwitchResponse_noData_fail(){
    // Arrange
    String twitchResponse =  "{\"data\":[]}";

    // Act
    var result = UserHelper.createUserFromTwitchResponse(twitchResponse);

    // Assert
    assertTrue(result.isEmpty());
  }


}
