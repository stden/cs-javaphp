package ru.ipo.dces.clientservercommunication;

import java.util.Date;

/**
 * Ответ: присоединились к соревнованию
 */
public class ConnectToContestResponse implements Response {

  public String          sessionID;

  /** Описание пользователя */
  public UserDescription user;

  public Date finishTime; 
}
