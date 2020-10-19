package net.gecore.streamerendpoints.service.utils;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.ReadContext;

public class JsonPathUtils {

  public static String badResult = "Sorry things can't work out, it's you not me.";

  public static String retrieveString(String content, String partToFind) {
    try {
      ReadContext context = JsonPath.parse(content);
      return context.read(partToFind);
    } catch (PathNotFoundException pnf) {
      System.out.println("Encountered an error while parsing: " + content);
      pnf.printStackTrace();
      return badResult;
    }
  }

  public static Integer retrieveInt(String content, String partToFind) {
    try {
      ReadContext context = JsonPath.parse(content);
      return context.read(partToFind);
    } catch (PathNotFoundException pnf) {
      System.out.println("Encountered an error while parsing: " + content);
      pnf.printStackTrace();
      return 0;
    }
  }

}
