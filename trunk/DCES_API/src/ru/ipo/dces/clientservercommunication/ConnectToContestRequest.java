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
  @BinInfo(phpDefaultValue="0")
  public int    contestID;

  /**
   * Login пользователя.
   * <p>Замечание. В системе может быть зарегистрировано много одинаковых логинов. Пользователь определяется
   * на основе пары полей contestID / login
   */
  @BinInfo(phpDefaultValue="'admin'")
  public String login;

  /**
   * Пароль пользователя.
   */
  @BinInfo(phpDefaultValue="'superpassword'")
  public String password;
}
