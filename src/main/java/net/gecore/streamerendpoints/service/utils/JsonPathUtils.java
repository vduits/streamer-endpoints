package net.gecore.streamerendpoints.service.utils;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.ReadContext;

public class JsonPathUtils {

  public static String badResult = "Sorry things can't work out, it's you not me.";
  public static String retrieveInformation(String content, String partToFind){
    String result;
    ReadContext context = JsonPath.parse(content);
    try{
      result= context.read(partToFind);
    }catch(PathNotFoundException pnf){
      result = badResult;
    }
    return result;
  }
}
