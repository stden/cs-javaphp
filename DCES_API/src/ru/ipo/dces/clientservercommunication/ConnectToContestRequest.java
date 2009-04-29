package ru.ipo.dces.clientservercommunication;

/**
 * Присоединиться к соревнованию
 */
public class ConnectToContestRequest implements Request {
  public int    contestID;
  public String login;
  public String password;
}
