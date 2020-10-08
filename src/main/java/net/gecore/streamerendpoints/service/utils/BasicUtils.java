package net.gecore.streamerendpoints.service.utils;

import net.gecore.streamerendpoints.service.twitch.TwitchAPIException;

public class BasicUtils {

  public static long stringToLong(String userIdString) throws TwitchAPIException {
    try {
      return Long.parseLong(userIdString);
    } catch (NumberFormatException ne) {
      throw new TwitchAPIException("A conversion error took place.");
    }
  }

  public static int stringToInt(String userIdString) throws TwitchAPIException {
    try {
      return Integer.parseInt(userIdString);
    } catch (NumberFormatException ne) {
      throw new TwitchAPIException("A conversion error took place.");
    }
  }

}
