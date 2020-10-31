package net.gecore.streamerendpoints.service.shared.component;

import java.util.List;
import java.util.Map;

public class ApiReply {

  private final Map<String, List<String>> headers;

  private final String body;

  public ApiReply(Map<String, List<String>> headers, String body) {
    this.headers = headers;
    this.body = body;
  }

  public Map<String, List<String>> getHeaders() {
    return headers;
  }

  public String getBody() {
    return body;
  }

}
