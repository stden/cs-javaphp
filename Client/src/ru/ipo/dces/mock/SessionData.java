package ru.ipo.dces.mock;

/**
 * ������ � ������� ������ ������������. ������-�� �������� �� �������, ��
 * ������-�������� MockServer ������ �� � ������ ����� ��������
 */
public class SessionData {

  public String login;
  public int    contestID;

  public SessionData(String login, int contestID) {
    this.login = login;
    this.contestID = contestID;
  }

}
