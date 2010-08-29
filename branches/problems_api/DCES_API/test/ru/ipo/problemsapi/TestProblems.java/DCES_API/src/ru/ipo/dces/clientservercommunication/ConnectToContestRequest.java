package ru.ipo.dces.clientservercommunication;

/**
 * ������ �� ����������� � ������������. �����������.
 */
public class ConnectToContestRequest implements Request {
  /**
   * id ����������
   * ��, � �������� ���������� �������������. 0 - ����������� ������������, � ���� ������������
   * ������������� �������, ����� ����� ����������� ���������������� ������
   */
  @PHPDefaultValue("0")
  public int    contestID;

  /**
   * Login ������������.
   * <p>���������. � ������� ����� ���� ���������������� ����� ���������� �������. ������������ ������������
   * �� ������ ���� ����� contestID / login
   */
  @PHPDefaultValue("'admin'")
  public String login;

  /**
   * ������ ������������.
   */
  @PHPDefaultValue("'superpassword'")
  public String password;
}
