package ru.ipo.dces.clientservercommunication;

/**
 * Запрос: Получить список пользователей
 */
public class GetUsersRequest implements Request {
  /**
   * Идентификатор сессии. Допускаютс идентификаторы только администраторов соревнования или сервера
   */
  @PHPDefaultValue("null")
  public String sessionID;

  /**
   * Номер соревнования, по которому требуется получить список пользователей. Актуален для администратора
   * сервера. Администратор соревнования указывает -1 или id своего соревнования
   */
  @PHPDefaultValue("null")
  public int contestID;
}
