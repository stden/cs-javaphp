package ru.ipo.dces.clientservercommunication;

/**
 * Запрос: Отключиться от сервера
 */
public class DisconnectRequest implements Request {

  /** Сессия пользователя, который хочет отлючиться от сервера */
  public String sessionID;

  public DisconnectRequest(String sessionID) {
    this.sessionID = sessionID;
  }
}
