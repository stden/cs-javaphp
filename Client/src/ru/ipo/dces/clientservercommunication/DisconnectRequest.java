package ru.ipo.dces.clientservercommunication;

/**
 * Запрос: Отключиться от сервера
 */
public class DisconnectRequest implements Request {
  public String sessionID;
}
