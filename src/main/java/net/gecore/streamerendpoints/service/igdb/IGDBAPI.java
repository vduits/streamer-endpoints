package net.gecore.streamerendpoints.service.igdb;

import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import net.gecore.streamerendpoints.service.shared.SharedAPI;
import net.gecore.streamerendpoints.service.shared.SharedApiException;
import net.gecore.streamerendpoints.service.shared.component.ApiReply;
import net.gecore.streamerendpoints.service.shared.component.SharedApiHelper;
import net.gecore.streamerendpoints.service.twitch.AuthService;
import net.gecore.streamerendpoints.service.twitch.TwitchAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IGDBAPI {

  private final SharedAPI sAPI;

  private final AuthService authService;

  private final Logger LOGGER = LoggerFactory.getLogger(IGDBAPI.class);

  public IGDBAPI(SharedAPI sAPI, AuthService authService) {
    this.sAPI = sAPI;
    this.authService = authService;
  }

  public String request(URL url, String body)
      throws IGDBAPIException {
    try {
      byte[] bytesBody = SharedApiHelper.bodyCreate(body);
      HttpsURLConnection con = sAPI.buildPOSTConnection(
          url, bytesBody, authService.provideAuthHeaders());
      ApiReply response = sAPI.readResponse(con);
      return response.getBody();
    } catch (SharedApiException | TwitchAPIException sae) {
      LOGGER.error(sae.getMessage());
      throw new IGDBAPIException("An error occurred trying to communicate with IGDB.");
    }

  }

}
