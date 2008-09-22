package ru.ipo.dces.clientservercommunication;

/**
 * Запрос на изменение пароля
 */
public class ChangePasswordRequest implements Request {

  public String sessionID;
  public String oldPassword;
  public String newPassword;

}
