package ru.ipo.dces.clientservercommunication;

/**
 * Ответ: присоединились к соревнованию
 */
public class ConnectToContestResponse implements Response {
  public String          sessionID;
  /** Данные о пользователе */
  public UserDescription user;
}
