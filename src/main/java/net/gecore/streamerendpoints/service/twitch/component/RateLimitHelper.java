package net.gecore.streamerendpoints.service.twitch.component;

import java.time.Instant;
import java.util.Optional;

public class RateLimitHelper {

  public static Optional<RateLimit> convertToRateLimit(String remaining, String resetTime){
    try {
      int remainingOnes = Integer.parseInt(remaining);
      Instant resultingTime = Instant.ofEpochSecond(Long.parseLong(resetTime));
      RateLimit rateLimit = new RateLimit(remainingOnes, resultingTime);
      return Optional.of(rateLimit);
    }catch (NumberFormatException ne){
      ne.printStackTrace();
      return Optional.empty();
    }
  }

}
