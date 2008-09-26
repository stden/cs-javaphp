package ru.ipo.dces.mock;

import ru.ipo.dces.clientservercommunication.*;

/**
 * ������ � ������� ������ ������������. ������-�� �������� �� �������, ��
 * ������-�������� MockServer ������ �� � ������ ����� ��������
 */
public class SessionData {

  public String               login;
  public String               password;
  public ContestDescription   contest;
  public ProblemDescription[] problems;

}
