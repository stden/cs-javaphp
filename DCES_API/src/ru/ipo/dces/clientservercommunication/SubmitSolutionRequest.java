package ru.ipo.dces.clientservercommunication;

import java.util.HashMap;

/**
 * Запрос: отправка решения на сервер
 */
public class SubmitSolutionRequest implements Request {
  /**
   * Идентификатор сессии участника. Посылать решение может участник и администратор соревнования.
   * При посылке решения администратором сервера поведение неопределено (это надо бы исправить)
   */
  public String  sessionID;
  /** Идентификатор задачи, по которой посылается решение. По этому идентификатору можно
   * восстановить и идентификатор соревнования */
  public int     problemID;
  /**
   * Решение участника. Данные хранятся в свободной форме и разбираются только плагином стороны сервера
   */
  public HashMap<String, String> problemResult;
}
