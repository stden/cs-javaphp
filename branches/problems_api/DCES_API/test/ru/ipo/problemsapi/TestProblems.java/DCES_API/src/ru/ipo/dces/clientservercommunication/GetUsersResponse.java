package ru.ipo.dces.clientservercommunication;

/**
 * Ответ: список участников соревнования
 */
public class GetUsersResponse implements Response {
  /**
   * Массив с описаниями участников
   */
  public UserDescription[] users;
}
