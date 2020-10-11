package net.gecore.streamerendpoints.service.twitch.component;

import java.net.MalformedURLException;
import java.net.URL;
import net.gecore.streamerendpoints.configuration.TwitchConfig;
import net.gecore.streamerendpoints.service.twitch.TwitchAPIException;
import net.gecore.streamerendpoints.service.twitch.constants.TwitchEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class URLHelper {

  public final static Logger LOGGER = LoggerFactory.getLogger(URLHelper.class);


  public static URL buildUrl(TwitchConfig twitchConfig, TwitchEndpoint twitchEndpoint,
      String urlParams) throws TwitchAPIException {
    try {
      return new URL(
          "https://api.twitch.tv/"
              + twitchConfig.getApiVersion()
              + "/"
              + twitchEndpoint.name()
              + urlParams);
    } catch (MalformedURLException me) {
      LOGGER.error("Encountered an issue trying to build the url: " + me.getMessage());
      throw new TwitchAPIException("There is configuration issue contacting the twitch api");
    }
  }

}
