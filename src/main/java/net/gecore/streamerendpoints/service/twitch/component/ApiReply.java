package net.gecore.streamerendpoints.service.twitch.component;

import java.util.List;
import java.util.Map;

public class ApiReply {

    private final Map<String, List<String>> headers;

    private final StringBuilder body;

    public ApiReply(Map<String, List<String>> headers, StringBuilder body){
        this.headers = headers;
        this.body = body;
    }

    public Map<String, List<String>> getHeaders(){
        return headers;
    }

    public String getBody(){
        return body.toString();
    }

}
