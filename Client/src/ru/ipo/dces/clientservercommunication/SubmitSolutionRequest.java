package ru.ipo.dces.clientservercommunication;

import java.util.HashMap;

/**
 * Запрос: отправка решения на сервер
 */
public class SubmitSolutionRequest implements Request {
  public String  sessionID;
  /** ID задачи, по которой посылается решение */
  public int     problemID; /*Содержит в себе заодно contestID*/
  public HashMap<String, String> problemResult;
}
