package net.gecore.streamerendpoints.processing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuoteParser {
  private Map<String, String> entities;

  @Autowired
  public QuoteParser(HTMLEntityFactory entityFactory) {
    this.entities = entityFactory.createHTMLEntities();
  }

  public String parse(String toParseString) {
    List<String> listOfStrings = new ArrayList<>();
    String[] splitString = toParseString.split("");
    for(String toCheckString : splitString){
        listOfStrings.add(escapeImportantShit(toCheckString));
    }
    String result = String.join("",listOfStrings);
    return result;
  }

  private String escapeImportantShit(String input) {
    if(entities.containsKey(input)){
      return entities.get(input);
    }
    return input;
  }

  public String unEscapeImportantShit(String input) {
    String output = input;
    for(Entry<String, String> entry: entities.entrySet()){
      if(output.contains(entry.getValue())){
        output = output.replace(entry.getValue(), entry.getKey());
      }
    }
    return output;
  }


}
