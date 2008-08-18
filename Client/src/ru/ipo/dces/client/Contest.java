package ru.ipo.dces.client;

public class Contest {

  public String name;

  public Contest(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }
}
