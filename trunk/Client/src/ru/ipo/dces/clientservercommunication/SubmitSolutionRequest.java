package ru.ipo.dces.clientservercommunication;

/**
 * Запрос: отправка решения на сервер
 */
public class SubmitSolutionRequest implements Request {
  public String  sessionID;
  /** ID задачи, по которой посылается решение */
  public String  problemID;
  public Request problemResult;
}
