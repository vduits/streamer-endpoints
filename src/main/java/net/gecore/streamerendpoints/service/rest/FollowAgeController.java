package net.gecore.streamerendpoints.service.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import net.gecore.streamerendpoints.processing.QuoteParser;

@RestController
@RequestMapping("${rest.api}" + "/followage")
public class FollowAgeController {

  private QuoteParser quoteParser;

  @Autowired
  public FollowAgeController( QuoteParser quoteParser){
    this.quoteParser = quoteParser;
  }



  //todo one that just grabs from a given integer/id
  @GetMapping(value = "/{followerId}/following/{streamerId}")
  public String retrieveAgeById(@PathVariable Integer followerId, @PathVariable Integer streamerId){
    String followAge = "Sorry can't help you";


    return followAge;
  }





  //todo one that grabs a string and checks the twitch api.
}
