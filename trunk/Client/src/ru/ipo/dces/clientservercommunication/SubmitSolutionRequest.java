package ru.ipo.dces.clientservercommunication;

import java.util.HashMap;

/**
 * Запрос: отправка решения на сервер
 */
public class SubmitSolutionRequest implements Request {
  public String  sessionID;
  /** ID контеста, по которому посылается решение. Содержится в session ID,
   *  но super admin способен посылать решение для любого контеста
   */
  public int     contestID;
  /** ID задачи, по которой посылается решение */
  public int     problemID;
  public HashMap<String, String> problemResult;
}
