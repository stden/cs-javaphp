package ru.ipo.dces.mock;

/**
 * ������ � ������� ������ ������������. ������-�� �������� �� �������, ��
 * ������-�������� MockServer ������ �� � ������ ����� ��������
 */
public class MockSessionServer {

  public String login;
  public int    contestID;

  public MockSessionServer(String login, int contestID) {
    this.login = login;
    this.contestID = contestID;
  }

}
