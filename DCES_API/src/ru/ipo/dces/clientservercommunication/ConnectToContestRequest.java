package ru.ipo.dces.clientservercommunication;

/**
 * «апрос на подключение к соревнованию. јвторизаци€.
 */
public class ConnectToContestRequest implements Request {
  /**
   * id соревнован
   * и€, к которому происходит подсоединение. 0 - виртуальное соревнование, к нему подключаетс€
   * администратор сервера, чтобы иметь возможность администрировать сервер
   */
  public int    contestID;

  /**
   * Login пользовател€.
   * <p>«амечание. ¬ системе может быть зарегистрировано много одинаковых логинов. ѕользователь определ€етс€
   * на основе пары полей contestID / login
   */
  public String login;

  /**
   * ѕароль пользовател€.
   */
  public String password;
}
