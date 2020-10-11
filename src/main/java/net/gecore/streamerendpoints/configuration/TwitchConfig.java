package net.gecore.streamerendpoints.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("twitch")
public class TwitchConfig {

  private String clientId;
  private String clientSecret;
  private String apiVersion;
  private int rateLimitThreshold;

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
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

}
