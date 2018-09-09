package net.gecore.streamerendpoints.processing;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class HTMLEntityFactory {
  protected Map<String,String> createHTMLEntities(){
    Map<String,String> resultedMap = new HashMap<>();
    resultedMap.put("&", "&amp;");
    resultedMap.put("?", "&#63");
    resultedMap.put("<", "&lt;");
    resultedMap.put(">", "&gt;");
    resultedMap.put("\"", "&quot;");
    resultedMap.put("'", "&#39;");
    resultedMap.put("/", "&#x2F;");
    resultedMap.put("`", "&#x60;");
    resultedMap.put("=", "&#x3D;");
    return resultedMap;
  }
}
