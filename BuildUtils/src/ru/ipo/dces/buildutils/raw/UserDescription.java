package ru.ipo.dces.buildutils.raw;

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
  @BinInfo(phpDefaultValue="null")
  public int      userID;

  /**
   * Логин
   */
  @BinInfo(phpDefaultValue="'test_login'")
  public String   login;

  /**
   * Пароль. В ответах сервера это поле не заполняется
   */
  @BinInfo(phpDefaultValue="'test_password'")
  public String   password;

  /**
   * Данные об участнике. Имя, школа, класс и все что угодно. Смыслы каждого
   * элемента массива хранятся в описании контеста. ContestDescription содержит
   * UserDataField[] data c информацией о полях с данными
   */
  @BinInfo(phpDefaultValue="array()")
  public String[] dataValue;

  /** Тип пользователя */
  //TODO избавиться от инициализатора
  @BinInfo(phpDefaultValue="'Participant'")
  public UserType userType = UserType.Participant;

}
