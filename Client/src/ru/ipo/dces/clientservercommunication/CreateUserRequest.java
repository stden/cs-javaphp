package ru.ipo.dces.clientservercommunication;

import ru.ipo.dces.client.Controller;

/**
 * �������� ������������
 */
public class CreateUserRequest implements Request {
  public String          sessionID;
  public UserDescription user;
}
