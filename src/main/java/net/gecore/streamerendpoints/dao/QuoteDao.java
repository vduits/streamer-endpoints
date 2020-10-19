package net.gecore.streamerendpoints.dao;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import net.gecore.streamerendpoints.domain.Quote;
import net.gecore.streamerendpoints.repostories.QuoteRepository;

@Component
public class QuoteDao {

  private final QuoteRepository quoteRepository;

  @Autowired
  public QuoteDao(QuoteRepository quoteRepository){
    this.quoteRepository = quoteRepository;
  }
  public Quote retrieveQuoteById(int quoteId){
    return quoteRepository.getById(quoteId);
  }

  public Quote save(Quote quote){
    return quoteRepository.save(quote);
  }

  public List<Quote> findAll(){
    return quoteRepository.findAll();
  }

  public int getMaxId(){
    return quoteRepository.getMaxId();
  }

}
