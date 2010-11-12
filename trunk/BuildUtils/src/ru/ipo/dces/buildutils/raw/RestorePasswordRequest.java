package ru.ipo.dces.buildutils.raw;

/**
 * Запрос: восстановление пароля. Пока не реализован
 */
public class RestorePasswordRequest implements Request {

  /**
   * Идентификатор соревнования, у пользователя которого восстанавливается пароль
   */
  @BinInfo(phpDefaultValue="null")
  public String contestID;

  /**
   * Логин пользователя, которому система восстанавливает пароль
   */
  @BinInfo(phpDefaultValue="'test_login'")
  public String login;
}
