package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: Илья
 * Date: 11.01.2009
 * Time: 18:34:37
 * <p/>
 * Запрос на изменение информации об одном из пользователей
 */
public class AdjustUserDataRequest implements Request {

  /**
   * id пользователя, информацию о котором требуется изменить
   */
  @PHPDefaultValue("0")
  public int userID;

  /**
   * Идентификатор сессии. Должен быть идентификатором администратора сервера или администратора соревнования,
   * которому принадлежит изменяемый пользователь.
   */
  @PHPDefaultValue("null")
  public String sessionID;

  /**
   * Новый login этого пользователя, либо null, если новый логин устанавливать не нужно
   */
  @PHPDefaultValue("'test_login'")
  public String login;

  /**
   * Новый пароль этого пользователя, либо null, если новый пароль устанавливать не нужно
   */
  @PHPDefaultValue("'test_password'")
  public String password;

  /**
   * Новый тип этого пользователя, либо null, если тип изменять не нужно
   */
  @PHPDefaultValue("'Participant'")
  public UserDescription.UserType newType;

  /**
   * Новая информация по пользователю (в соответствии с полем data из ContestDescription - описание соревнования,
   * к которому относится изменяемый пользователь).
   * Если информацию по пользователю изменять не нужно, в поле userData подставляется null 
   */
  @PHPDefaultValue("array()")
  public String[] userData;
}
