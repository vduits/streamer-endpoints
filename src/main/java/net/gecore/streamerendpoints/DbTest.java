package net.gecore.streamerendpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import net.gecore.streamerendpoints.repostories.QuoteRepository;

@Component
public class DbTest {

  private QuoteRepository quoteRepository;

  @Autowired
  public DbTest(QuoteRepository quoteRepository){
    this.quoteRepository = quoteRepository;
  }


  public void testRun(){
    System.out.println(quoteRepository.getById(2));
  }

}
