package net.gecore.streamerendpoints.service.twitch;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
public class ResponseAPI {

    private final Map<String, List<String>> headers;

    private final StringBuilder body;

    public ResponseAPI(Map<String, List<String>> headers, StringBuilder body){
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
