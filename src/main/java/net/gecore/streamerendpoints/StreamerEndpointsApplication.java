package net.gecore.streamerendpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import net.gecore.streamerendpoints.processing.QuoteParser;

@SpringBootApplication
public class StreamerEndpointsApplication implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(StreamerEndpointsApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    System.out.println("Streamer Endpoints Now Enabled");
  }

}
