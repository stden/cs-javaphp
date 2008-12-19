package ru.ipo.dces.clientservercommunication;

import ru.ipo.dces.clientservercommunication.ContestDescription;

/**
 * Создание нового контеста
 */
public class CreateContestRequest implements Request {

  public String             sessionID;
  public ContestDescription contest;

}
