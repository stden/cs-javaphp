package ru.ipo.dces.clientservercommunication;

import ru.ipo.dces.client.ClientData;

/**
 * Создание нового контеста
 */
public class CreateContestRequest implements Request {

  public String             sessionID;
  public ContestDescription contest;

  public CreateContestRequest(String contestName) {
    sessionID = ClientData.sessionID;
    contest = new ContestDescription(contestName);
  }

}
