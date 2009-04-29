package ru.ipo.dces.clientservercommunication;

/**
 * Запрос: удалить контест
 */
public class RemoveContestRequest implements Request {
  public String sessionID;
  public int contestID;
}