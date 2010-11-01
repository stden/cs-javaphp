package ru.ipo.dces.clientservercommunication;

/**
 * Запрос на подключение к соревнованию. Авторизация.
 */
public class ConnectToContestRequest implements Request {
  /**
   * id соревнован
   * ия, к которому происходит подсоединение. 0 - виртуальное соревнование, к нему подключается
   * администратор сервера, чтобы иметь возможность администрировать сервер
   */
  @PHPDefaultValue("0")
  public int    contestID;

  /**
   * Login пользователя.
   * <p>Замечание. В системе может быть зарегистрировано много одинаковых логинов. Пользователь определяется
   * на основе пары полей contestID / login
   */
  @PHPDefaultValue("'admin'")
  public String login;

  /**
   * Пароль пользователя.
   */
  @PHPDefaultValue("'superpassword'")
  public String password;
}
