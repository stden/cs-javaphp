package ru.ipo.dces.clientservercommunication;

/**
 * ������: �������� ������ �������������
 */
public class GetUsersRequest implements Request {
  /**
   * ������������� ������. ���������� �������������� ������ ��������������� ������������ ��� �������
   */
  @PHPDefaultValue("null")
  public String sessionID;

  /**
   * ����� ������������, �� �������� ��������� �������� ������ �������������. �������� ��� ��������������
   * �������. ������������� ������������ ��������� -1 ��� id ������ ������������
   */
  @PHPDefaultValue("null")
  public int contestID;
}
