package ru.ipo.dces.clientservercommunication;

/**
 * Информация о пользователе. Не является запросом или ответом, но содержится в них
 */
public class UserDescription {

  /**
   * Тип пользователя в системе. От типа зависит набор доступных пользователю действий
   */
  public enum UserType {
    /**
     * Участник
     */
    Participant,
    /**
     * Администратор соревнования
     */
    ContestAdmin,
    /**
     * Администратор сервера
     */
    SuperAdmin,
  }

  /**
   * Идентификатор пользователя
   */
  public int      userID;

  /**
   * Логин
   */
  public String   login;

  /**
   * Пароль. В ответах сервера это поле не заполняется
   */
  public String   password;

  /**
   * Данные об участнике. Имя, школа, класс и все что угодно. Смыслы каждого
   * элемента массива хранятся в описании контеста. ContestDescription содержит
   * UserDataField[] data c информацией о полях с данными
   */
  public String[] dataValue;

  /** Тип пользователя */
  //TODO избавиться от инициализатора
  public UserType userType = UserType.Participant;

}
