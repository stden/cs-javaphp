package ru.ipo.dces.buildutils.raw;

/**
 * Запрос на отключиться от соревнования
 */
public class DisconnectRequest implements Request {

  /** Сессия пользователя, который хочет отлючиться от сервера */
  @BinInfo(phpDefaultValue="null")
  public String sessionID;

}
