package ru.ipo.dces.clientservercommunication;

/**
 * Создание нового контеста
 */
public class CreateContestRequest implements Request {

  public String             sessionID;
  public ContestDescription contest;

  public CreateContestRequest(String contestName) {
    contest = new ContestDescription(contestName);
  }

}
