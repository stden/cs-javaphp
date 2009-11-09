package ru.ipo.dces.clientservercommunication;

/**
 * Запрос: восстановление пароля. Пока не реализован
 */
public class RestorePasswordRequest implements Request {

  /**
   * Идентификатор соревнования, у пользователя которого восстанавливается пароль
   */
  @PHPDefaultValue("null")
  public String contestID;

  /**
   * Логин пользователя, которому система восстанавливает пароль
   */
  @PHPDefaultValue("'test_login'")
  public String login;
}
