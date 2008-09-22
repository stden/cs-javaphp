package ru.ipo.dces.clientservercommunication;

/**
 * Запрос: восстановление пароля
 */
public class RestorePasswordRequest implements Request {
  public String contestID;
  public String login;
}
