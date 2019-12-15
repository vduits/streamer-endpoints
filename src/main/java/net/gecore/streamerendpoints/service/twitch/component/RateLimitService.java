package net.gecore.streamerendpoints.service.twitch.component;

import java.util.Objects;
import java.util.Optional;
import net.gecore.streamerendpoints.configuration.TwitchConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RateLimitService {

  private RateLimit currentRateLimit;
  private final int threshold;

  @Autowired
  public RateLimitService(TwitchConfiguration configuration){
    this.threshold = configuration.getRateLimitThreshold();
  }

  public boolean canPerformRequest(){
    if(Objects.nonNull(currentRateLimit)){
      return this.threshold < this.currentRateLimit.getRemaining();
    }else{
      return false;
    }
  }

  public Optional<RateLimit> returnRateLimit(){
    if(Objects.nonNull(this.currentRateLimit)){
      return Optional.of(this.currentRateLimit);
    }else{
      return Optional.empty();
    }
  }
  public void updateRateLimit(RateLimit rateLimit){
    this.currentRateLimit = rateLimit;
  }


}
