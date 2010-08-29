package ru.ipo.dces.clientservercommunication;

/**
 * ������: ������� ������������
 */
public class RemoveUserRequest implements Request {

  /**
   * ������������� ������ �������������� ������������, �������� ����������� ������������, ��� ��������������
   * �������
   */
  @PHPDefaultValue("null")
  public String sessionID;

  /**
   * ������������� ���������� ������������
   */
  @PHPDefaultValue("null")
  public int userID;
}
