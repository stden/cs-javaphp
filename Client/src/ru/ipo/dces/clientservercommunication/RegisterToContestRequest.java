package ru.ipo.dces.clientservercommunication;

/**
 * ������������������ ��� ������� � ��������
 */
public class RegisterToContestRequest implements Request {
  public String sessionID; //leave it null if this request is anonymous
  public int contestID;
  public UserDescription user;
}
