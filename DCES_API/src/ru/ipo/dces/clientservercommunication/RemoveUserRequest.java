package ru.ipo.dces.clientservercommunication;

/**
 * Запрос: удалить пользователя
 */
public class RemoveUserRequest implements Request {

  /**
   * Идентификатор сессии администратора соревнования, которому принадлежит пользователь, или администратора
   * сервера
   */
  public String sessionID;

  /**
   * Идентификатор удаляемого пользователя
   */
  public int userID;
}
