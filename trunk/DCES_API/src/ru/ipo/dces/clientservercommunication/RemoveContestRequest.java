package ru.ipo.dces.clientservercommunication;

/**
 * Запрос: удалить соревнование
 */
public class RemoveContestRequest implements Request {

  /**
   * Идентификатор сессии администратора сервера
   */
  @BinInfo(phpDefaultValue="null")
  public String sessionID;

  /**
   * Идентификатор удаляемого соревнования
   */
  @BinInfo(phpDefaultValue="null")
  public int contestID;
}