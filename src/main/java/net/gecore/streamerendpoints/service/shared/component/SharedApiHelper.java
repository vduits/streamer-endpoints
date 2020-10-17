package net.gecore.streamerendpoints.service.shared.component;

import java.nio.charset.StandardCharsets;

public class SharedApiHelper {

    public static byte[] bodyCreate(String body){
        return body.getBytes(StandardCharsets.UTF_8);
    }

}
