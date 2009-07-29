package ru.ipo.dces.clientservercommunication;

/**
 * Запрос: удалить соревнование
 */
public class RemoveContestRequest implements Request {

  /**
   * Идентификатор сессии администратора сервера
   */
  public String sessionID;

  /**
   * Идентификатор удаляемого соревнования
   */
  public int contestID;
}