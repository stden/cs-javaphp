package ru.ipo.dces.clientservercommunication;

/**
 * Информация о пользователе
 */
public class UserDescription {

  public String login;
  public String password;
  /** имя участника */
  public String name;
  /**
   * организация, т.е. школа, университет и т.п. можно сюда добавлять
   * дополнительные поля. Это не очень хороший вариант расширяемости, но хоть
   * как
   */
  public String institution;
  /**
   * чтобы всегда была возможность написать дополнительную информацию, если она
   * почему-то не входит в набор стандартных полей
   */
  public String extraInformation;
}
