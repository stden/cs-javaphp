package ru.ipo.dces.clientservercommunication;

/**
 * ������ �� ��������� ������
 */
public class ChangePasswordRequest implements Request {

  public String sessionID;
  public String oldPassword;
  public String newPassword;

}
