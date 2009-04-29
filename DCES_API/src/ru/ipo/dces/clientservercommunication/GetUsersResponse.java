package ru.ipo.dces.clientservercommunication;

/**
 * Ответ: список пользователей сервера
 */
public class GetUsersResponse implements Response {
  public UserDescription[] users;
}
