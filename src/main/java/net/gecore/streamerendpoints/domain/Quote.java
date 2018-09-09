package net.gecore.streamerendpoints.domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Quote {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String quote;
  private LocalDateTime reg_date;

  public Quote() {
  }

  public Quote(String quote) {
    this.quote = quote;
    this.reg_date = LocalDateTime.now();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getQuote() {
    return quote;
  }

  public void setQuote(String quote) {
    this.quote = quote;
  }

  public LocalDateTime getReg_date() {
    return reg_date;
  }

  public void setReg_date(LocalDateTime reg_date) {
    this.reg_date = reg_date;
  }


}
