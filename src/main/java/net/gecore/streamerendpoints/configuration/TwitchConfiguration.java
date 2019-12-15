package net.gecore.streamerendpoints.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("twitch")
public class TwitchConfiguration {

  private String clientId;
  private String apiVersion;
  private int rateLimitThreshold;
  private long configuredUser;

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getApiVersion() {
    return apiVersion;
  }

  public void setApiVersion(String apiVersion) {
    this.apiVersion = apiVersion;
  }

  public int getRateLimitThreshold() {
    return rateLimitThreshold;
  }

  public void setRateLimitThreshold(int rateLimitThreshold) {
    this.rateLimitThreshold = rateLimitThreshold;
  }

  public long getConfiguredUser() {
    return configuredUser;
  }

  public void setConfiguredUser(long configuredUser) {
    this.configuredUser = configuredUser;
  }
}
