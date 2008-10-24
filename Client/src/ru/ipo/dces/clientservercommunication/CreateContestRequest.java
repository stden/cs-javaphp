package ru.ipo.dces.clientservercommunication;

import ru.ipo.dces.client.Controller;

/**
 * Создание нового контеста
 */
public class CreateContestRequest implements Request {

  public String             sessionID;
  public ContestDescription contest;

  public CreateContestRequest(String contestName) {
    sessionID = Controller.sessionID;
    contest = new ContestDescription(contestName);
  }

}
