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
  @PHPDefaultValue("'test_login'")
  public String login;

  /**
   * ������ ������������.
   */
  @PHPDefaultValue("'test_password'")
  public String password;
}
