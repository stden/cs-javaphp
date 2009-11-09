package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: Илья
 * Date: 11.12.2008
 * Time: 23:05:01
 *
 * Запрос на первоначальное создание базы данных на сервере. Указывается логин и пароль администратора
 * сервера. Другие администраторы сервера могут быть добавлены позже 
 */
public class CreateDataBaseRequest implements Request {

  /**
   * Логин администратора сервера
   */
  @PHPDefaultValue("admin")
  public String login;

  /**
   * Пароль администратора сервера
   */
  @PHPDefaultValue("superpassword")
  public String password;

}
