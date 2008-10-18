package ru.ipo.dces.clientservercommunication;

/**
 * Информация о пользователе
 */
public class UserDescription {

  public enum UserType {
    User, ContestAdmin, SuperAdmin
  }

  public String   login;
  public String   password;

  /**
   * Данные об участнике. Имя, школа, класс и все что угодно. Смыслы каждого
   * элемента массива хранятся в описании контеста. ContestDescription содержит
   * String[] data c информацией вида {'Имя','Фамилия','Школа','Класс'}
   */
  public String[] dataValue;

  /** Тип пользователя */
  public UserType userType = UserType.User;

}
