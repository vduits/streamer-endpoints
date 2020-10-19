package net.gecore.streamerendpoints.service.utils;

import java.net.MalformedURLException;
import java.net.URL;
import net.gecore.streamerendpoints.configuration.TwitchConfig;
import net.gecore.streamerendpoints.service.igdb.IGDBAPIException;
import net.gecore.streamerendpoints.service.igdb.constants.IGDBEndpoint;
import net.gecore.streamerendpoints.service.twitch.TwitchAPIException;
import net.gecore.streamerendpoints.service.twitch.constants.TwitchEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class URLHelper {

  public final static Logger LOGGER = LoggerFactory.getLogger(URLHelper.class);


  public static URL buildTwitchUrl(TwitchConfig twitchConfig, TwitchEndpoint twitchEndpoint,
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

  public static URL buildIGDBUrl(IGDBEndpoint endpoint) throws IGDBAPIException {
    try {
      return new URL(
          "https://api.igdb.com/v4/"
              + endpoint
      );
    } catch (MalformedURLException me) {
      LOGGER.error("Encountered an issue trying to build the url: " + me.getMessage());
      throw new IGDBAPIException("There is a configuration issue contacting the IGDB api");
    }
  }

}
