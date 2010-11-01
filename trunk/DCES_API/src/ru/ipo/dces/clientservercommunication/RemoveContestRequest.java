package ru.ipo.dces.clientservercommunication;

/**
 * Запрос: удалить соревнование
 */
public class RemoveContestRequest implements Request {

  /**
   * Идентификатор сессии администратора сервера
   */
  @PHPDefaultValue("null")
  public String sessionID;

  /**
   * Идентификатор удаляемого соревнования
   */
  @PHPDefaultValue("null")
  public int contestID;
}