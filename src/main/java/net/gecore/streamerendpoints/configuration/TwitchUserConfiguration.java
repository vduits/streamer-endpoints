package net.gecore.streamerendpoints.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("userconfig")
public class TwitchUserConfiguration {

  private int userId;


  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getUserId() {
    return this.userId;
  }
}
