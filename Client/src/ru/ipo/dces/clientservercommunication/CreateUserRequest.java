package ru.ipo.dces.clientservercommunication;

/**
 * Создание пользователя
 */
public class CreateUserRequest implements Request {
  public String          sessionID;
  public UserDescription user;

  public CreateUserRequest(String login, String password) {
    user = new UserDescription();
    user.login = login;
    user.password = password;
  }
}
