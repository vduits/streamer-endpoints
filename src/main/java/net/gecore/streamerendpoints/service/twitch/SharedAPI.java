package net.gecore.streamerendpoints.service.twitch;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class SharedAPI {

    private final Logger LOGGER = LoggerFactory.getLogger(SharedAPI.class);

    public SharedAPI(){

    }

    public HttpsURLConnection buildConnection(URL url, HttpMethod httpMethod,
                                               Map<String, String> headers) throws  TwitchAPIException{
        HttpsURLConnection conn;
        try {
            conn = (HttpsURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod(httpMethod.name());
            if(!headers.isEmpty()){
                addHeaders(conn, headers);
            }
        } catch (ProtocolException pe) {
            LOGGER.error(pe.getMessage());
            throw new TwitchAPIException("Protocol error encountered");
        } catch (IOException io) {
            LOGGER.error(io.getMessage());
            throw new TwitchAPIException("Connection with twitch got interrupted");
        }
        return conn;
    }

    public void addHeaders(HttpsURLConnection conn, Map<String, String> headers){
        for(Map.Entry<String,String> entry : headers.entrySet()){
            conn.setRequestProperty(entry.getKey(), entry.getValue());
        }
    }

    public APIData readResponse(HttpsURLConnection con){
        StringBuilder content = new StringBuilder();
        APIData rAPI;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String input;
            while ((input = br.readLine()) != null) {
                content.append(input);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        con.disconnect();
        rAPI = new APIData(con.getHeaderFields(), content);
        return rAPI;
    }
}
