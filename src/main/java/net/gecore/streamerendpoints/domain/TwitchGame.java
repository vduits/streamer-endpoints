package net.gecore.streamerendpoints.domain;

public class TwitchGame {

  private long id;
  private String name;

  public TwitchGame() {
  }

  public TwitchGame(long id, String name) {
    this.id = id;
    this.name = name;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
