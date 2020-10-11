package net.gecore.streamerendpoints.service.twitch;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import net.gecore.streamerendpoints.configuration.TwitchConfig;
import net.gecore.streamerendpoints.service.twitch.component.RateLimit;
import net.gecore.streamerendpoints.service.twitch.component.RateLimitHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RateLimitService {

  private RateLimit currentRateLimit;
  private static final int DEFAULT_LIMIT = 800;
  private final int threshold;

  private final Logger LOGGER = LoggerFactory.getLogger(RateLimitService.class);

  /**
   * Creates new RateLimitService with a basic ratelimit that is reduced by the standard twitch
   * limit by the threshold set in the twitch configuration.
   *
   * @param configuration TwitchConfiguration
   */

  @Autowired
  public RateLimitService(TwitchConfig configuration) {
    this.threshold = configuration.getRateLimitThreshold();
    this.currentRateLimit = new RateLimit(DEFAULT_LIMIT - threshold, Instant.now());
  }

  public boolean canPerformRequest() {
    if (Objects.nonNull(currentRateLimit)) {
      return this.threshold < this.currentRateLimit.getRemaining();
    } else {
      return false;
    }
  }

  public Optional<RateLimit> returnRateLimit() {
    if (Objects.nonNull(this.currentRateLimit)) {
      return Optional.of(this.currentRateLimit);
    } else {
      return Optional.empty();
    }
  }

  public void updateRateLimit(Map<String, List<String>> headers) {
    var remaining = headers.get("Ratelimit-Remaining");
    var resetTime = headers.get("Ratelimit-Reset");
    Optional<RateLimit> rateLimit = RateLimitHelper.convertToRateLimit(
        remaining.get(0), resetTime.get(0));
    if (rateLimit.isPresent()) {
      this.currentRateLimit = rateLimit.get();
    } else {
      LOGGER.error("Could not determine rate limit");
    }
  }


}
