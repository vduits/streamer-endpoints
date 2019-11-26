package net.gecore.streamerendpoints.service.twitch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import net.gecore.streamerendpoints.configuration.TwitchConfiguration;
import net.gecore.streamerendpoints.service.utils.JsonPathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TwitchAPI {

  private TwitchConfiguration twitchConfiguration;
  private static String CLIENTIDHEADER = "Client-ID";
  private static String TWITCHAPI = "https://api.twitch.tv/";
  private static String LOGINPARAMETER = "?login=";
  private static String FOLLOWSPARAMETER = "/follows?";
  private static String TOID = "to_id=";
  private static String FROMID = "from_id=";

  private static String JPATHLOGINDATA = "$.data[0].id";
  private static String JPATHFOLLOWERDATE = "$.data[0].followed_at";

  @Autowired
  public TwitchAPI(TwitchConfiguration twitchConfiguration) {
    this.twitchConfiguration = twitchConfiguration;
  }

  public String request(Endpoint endpoint, String urlParams) throws TwitchAPIException{
    HttpsURLConnection con = buildConnection(endpoint, urlParams);
    return readResponse(con);
  }

  private HttpsURLConnection buildConnection(Endpoint endpoint, String urlParams)
      throws  TwitchAPIException{
    URL url;
    HttpsURLConnection con;
    try {
      url = new URL(
          TWITCHAPI
              + twitchConfiguration.getApiVersion()
              + "/"
              + endpoint.name()
              + urlParams);
      con = (HttpsURLConnection) url.openConnection();
      con.setConnectTimeout(5000);
      con.setReadTimeout(5000);
      con.setRequestMethod("GET");
      con.setRequestProperty(CLIENTIDHEADER, twitchConfiguration.getClientId());
    } catch (MalformedURLException me) {
      throw new TwitchAPIException("Url format is messed up");
    } catch (ProtocolException pe) {
      throw new TwitchAPIException("Sorry connection is complaining");
    } catch (IOException io) {
      throw new TwitchAPIException("Sorry twitch is complaining");
    }
    return con;
  }

  private String readResponse(HttpsURLConnection con){
    StringBuilder content = new StringBuilder();
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
      String input;
      while ((input = br.readLine()) != null) {
        content.append(input);
      }
      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    con.disconnect();
    return content.toString();
  }

  public String retrieveFollowAge(int followerId, int streamerId) throws TwitchAPIException{
    String urlBuildUp = FOLLOWSPARAMETER+TOID+streamerId+"&"+FROMID+followerId;
    String response = request(Endpoint.users, urlBuildUp);
    return JsonPathUtils.retrieveInformation(response, JPATHFOLLOWERDATE);
  }

  public String retrieveUserFromName(String userName) throws TwitchAPIException{
    String response  = request(Endpoint.users, LOGINPARAMETER +userName);
    return JsonPathUtils.retrieveInformation(response, JPATHLOGINDATA);
  }

}
