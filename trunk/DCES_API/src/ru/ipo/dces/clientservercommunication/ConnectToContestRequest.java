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
  public int    contestID;

  /**
   * Login ������������.
   * <p>���������. � ������� ����� ���� ���������������� ����� ���������� �������. ������������ ������������
   * �� ������ ���� ����� contestID / login
   */
  public String login;

  /**
   * ������ ������������.
   */
  public String password;
}
