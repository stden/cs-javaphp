package ru.ipo.dces.plugin;

import ru.ipo.dces.clientservercommunication.Request;

public class Answer implements Request {

  public String answer;

  public Answer(String answer) {
    this.answer = answer;
  }

  @Override
  public String toString() {
    return answer;
  }

}
