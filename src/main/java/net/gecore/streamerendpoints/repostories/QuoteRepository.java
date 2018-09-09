package net.gecore.streamerendpoints.repostories;



import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import net.gecore.streamerendpoints.domain.Quote;

@Repository
public interface QuoteRepository extends CrudRepository<Quote, Integer> {


  List<Quote> findAll();
  Quote getById(Integer id);

  Quote save(Quote quote);

  @Query("SELECT coalesce(max(q.id), 0) FROM Quote q")
  int getMaxId();

}
