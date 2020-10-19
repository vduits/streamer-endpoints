package net.gecore.streamerendpoints.service.igdb.constants;

public enum ExternalGameCategory {
  TWITCH(14);

  private final int value;

  ExternalGameCategory(int value){
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
