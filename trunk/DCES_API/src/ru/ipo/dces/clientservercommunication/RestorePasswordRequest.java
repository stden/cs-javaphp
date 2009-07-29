package ru.ipo.dces.clientservercommunication;

/**
 * Запрос: восстановление пароля. Пока не реализован
 */
public class RestorePasswordRequest implements Request {
  public String contestID;
  public String login;
}
