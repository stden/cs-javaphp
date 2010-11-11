package ru.ipo.dces.clientservercommunication;

/**
 * Запрос: удалить пользователя
 */
public class RemoveUserRequest implements Request {

  /**
   * Идентификатор сессии администратора соревнования, которому принадлежит пользователь, или администратора
   * сервера
   */
  @BinInfo(phpDefaultValue="null")
  public String sessionID;

  /**
   * Идентификатор удаляемого пользователя
   */
  @BinInfo(phpDefaultValue="null")
  public int userID;
}
