package ru.ipo.dces.clientservercommunication;

/**
 * Запрос: удалить пользователя
 */
public class RemoveUserRequest implements Request {
  public String sessionID;
  public int userID;
}
