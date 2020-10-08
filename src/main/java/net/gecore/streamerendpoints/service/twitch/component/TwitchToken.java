package net.gecore.streamerendpoints.service.twitch.component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TwitchToken {

  private final String token;
  private final int expiresInSeconds;
  private final LocalDateTime createdAt;
  private final LocalDateTime expiresAt;

  public TwitchToken(String token, int expiresInSeconds) {
    this.token = token;
    this.expiresInSeconds = expiresInSeconds;
    var now = LocalDateTime.now();
    this.createdAt = now;
    this.expiresAt = now.plus(expiresInSeconds, ChronoUnit.SECONDS);
  }

  public String getToken() {
    return token;
  }

  public int getExpiryTimeStamp() {
    return expiresInSeconds;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }


  public LocalDateTime getExpiresAt() {
    return expiresAt;
  }
}
