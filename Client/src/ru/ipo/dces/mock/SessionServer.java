package ru.ipo.dces.mock;

/**
 * ������ � ������� ������ ������������. ������-�� �������� �� �������, ��
 * ������-�������� MockServer ������ �� � ������ ����� ��������
 */
public class SessionServer {

  public String login;
  public int    contestID;

  public SessionServer(String login, int contestID) {
    this.login = login;
    this.contestID = contestID;
  }

}
