package net.gecore.streamerendpoints.service.twitch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import org.springframework.beans.factory.annotation.Value;

public class TwitchAPI {

  @Value("${twitch.client-id:}")
  private String clientId;
  private static String CLIENTIDHEADER = "Client-ID";


  public String request(String urlParams) {
    String result = "whoops";
    String response = "";
    URL url;
    HttpsURLConnection con;

    try {
      url = new URL("https://twitch.com/" + urlParams);
      con = (HttpsURLConnection) url.openConnection();
      con.setRequestMethod("GET");
      con.setRequestProperty(CLIENTIDHEADER, clientId);
    } catch (MalformedURLException me) {
      return "Url format is messed up";
    } catch (ProtocolException pe) {
      return "Sorry connection is complaining";
    } catch (IOException io) {
      return "Sorry twitch is complaining";
    }

    try {
      BufferedReader br =
          new BufferedReader(
              new InputStreamReader(con.getInputStream()));
      String input;

      while ((input = br.readLine()) != null) {
        response = response + input;
        System.out.println(input);
      }
      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return result;
  }


  public String retrieveFollowAge(int followerId, int followedId) {
    // todo turn two ids into a request for the urlparams
    //https://api.twitch.tv/helix/users/follows?to_id=44566682&from_id=62013306
  /*
    {
    "total": 1,
    "data": [
        {
            "from_id": "62013306",
            "to_id": "44566682",
            "followed_at": "2016-11-18T16:59:30Z"
        }
    ],
    "pagination": {
        "cursor": "eyJiIjpudWxsLCJhIjoiIn0"
    }
    }
   */
    String result = "";

    return result;
  }

  public int retrieveUserFromName(String userName) {
    //todo method that checks an username for an id.
    //https://api.twitch.tv/helix/users?login=KathoriasTV
  /* example result
  {
    "data": [
        {
            "id": "110473115",
            "login": "kathoriastv",
            "display_name": "KathoriasTV",
            "type": "",
            "broadcaster_type": "",
            "description": "",
            "profile_image_url": "https://static-cdn.jtvnw.net/jtv_user_pictures/48411c5f-95a6-44cf-9e5d-100830f7d37f-profile_image-300x300.png",
            "offline_image_url": "",
            "view_count": 137
        }
    ]
  }

   */

    return 0;
  }

}
