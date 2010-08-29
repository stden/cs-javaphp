package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: »ль€
 * Date: 11.01.2009
 * Time: 18:34:37
 * <p/>
 * «апрос на изменение информации об одном из пользователей
 */
public class AdjustUserDataRequest implements Request {

  /**
   * id пользовател€, информацию о котором требуетс€ изменить
   */
  @PHPDefaultValue("0")
  public int userID;

  /**
   * »дентификатор сессии. ƒолжен быть идентификатором администратора сервера или администратора соревновани€,
   * которому принадлежит измен€емый пользователь.
   */
  @PHPDefaultValue("null")
  public String sessionID;

  /**
   * Ќовый login этого пользовател€, либо null, если новый логин устанавливать не нужно
   */
  @PHPDefaultValue("'test_login'")
  public String login;

  /**
   * Ќовый пароль этого пользовател€, либо null, если новый пароль устанавливать не нужно
   */
  @PHPDefaultValue("'test_password'")
  public String password;

  /**
   * Ќовый тип этого пользовател€, либо null, если тип измен€ть не нужно
   */
  @PHPDefaultValue("'Participant'")
  public UserDescription.UserType newType;

  /**
   * Ќова€ информаци€ по пользователю (в соответствии с полем data из ContestDescription - описание соревновани€,
   * к которому относитс€ измен€емый пользователь).
   * ≈сли информацию по пользователю измен€ть не нужно, в поле userData подставл€етс€ null 
   */
  @PHPDefaultValue("array()")
  public String[] userData;
}
