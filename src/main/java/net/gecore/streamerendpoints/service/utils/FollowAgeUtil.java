package net.gecore.streamerendpoints.service.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FollowAgeUtil {

  public static String calculate(String startDate) {
    String timeFrame = "";
    DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    LocalDateTime lda = null;

    try {
      lda = LocalDateTime.parse(startDate, formatter);
    } catch (DateTimeParseException dte) {
      System.out.println(dte.getMessage());
    }
    if (lda != null) {
      LocalDate oldTime = lda.toLocalDate();
      LocalDate now = LocalDate.now(ZoneOffset.UTC);
      Period p = Period.between(oldTime, now);
      if (p.getYears() > 0) {
        timeFrame = timeFrame + p.getYears() + " years, ";
      }
      if (p.getMonths() > 0) {
        timeFrame = timeFrame + p.getMonths() + " months, ";
      }
      if (p.getDays() >= 0) {
        timeFrame = timeFrame + p.getDays() + " days";
      }
      return timeFrame;
    } else {
      return "something went wrong on the date calculator";
    }
  }

}
