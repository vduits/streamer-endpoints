package net.gecore.streamerendpoints.controller;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import net.gecore.streamerendpoints.dao.QuoteDao;
import net.gecore.streamerendpoints.domain.Quote;
import net.gecore.streamerendpoints.processing.QuoteParser;

@RestController
@RequestMapping("${rest.api}" + "/quote")
public class QuotesController {

  private QuoteDao quoteDao;
  private QuoteParser quoteParser;

  @Autowired
  public QuotesController(QuoteDao quoteDao, QuoteParser quoteParser){
    this.quoteDao = quoteDao;
    this.quoteParser = quoteParser;
  }


  @GetMapping(value = "")
  public List<Quote> retrieveAllQuotes() {
    List<Quote> allQuotes = quoteDao.findAll();
    return allQuotes;
  }

  @GetMapping(value= "/{quoteId}")
  public Quote retrieveCompleteQuote(@PathVariable Integer quoteId){
    return quoteDao.retrieveQuoteById(quoteId);
  }

  @GetMapping(value= "/{quoteId}/quote")
  public String retrieveQuote(@PathVariable Integer quoteId){
    Quote retrievedQuote = quoteDao.retrieveQuoteById(quoteId);
    if(Objects.nonNull(retrievedQuote)) {
      return quoteParser
          .unEscapeImportantShit(retrievedQuote.getId() + ": " + retrievedQuote.getQuote());
    }else{
      return "quote does not exist (anymore)";
    }
  }

  @GetMapping(value= "/random")
  public String retrieveRandomQuote(){
    Random random = new Random();
    int quoteId = random.nextInt( quoteDao.getMaxId() + 1 - 1) + 1;
    return retrieveQuote(quoteId);
  }


  @GetMapping(value = "/add")
  public String createQuote(@RequestParam String quote) {
    Quote thatQuote = new Quote(quoteParser.parse(quote));
    quoteDao.save(thatQuote);
    return "Ok, it is Quote: #" + thatQuote.getId();
  }


}
