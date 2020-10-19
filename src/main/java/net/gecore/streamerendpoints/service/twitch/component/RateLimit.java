package net.gecore.streamerendpoints.service.twitch.component;

import java.time.Instant;

public class RateLimit {

  private int remaining;
  private Instant resetTime;

  public RateLimit(int remaining, Instant resetTime) {
    this.remaining = remaining;
    this.resetTime = resetTime;
  }

  public int getRemaining() {
    return remaining;
  }

  public void setRemaining(int remaining) {
    this.remaining = remaining;
  }

  public Instant getResetTime() {
    return resetTime;
  }

  public void setResetTime(Instant resetTime) {
    this.resetTime = resetTime;
  }
}
