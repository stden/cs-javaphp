package ru.ipo.dces.clientservercommunication;

import java.util.HashMap;

/**
 * ������: �������� ������� �� ������
 */
public class SubmitSolutionRequest implements Request {
  /**
   * ������������� ������ ���������. �������� ������� ����� �������� � ������������� ������������.
   * ��� ������� ������� ��������������� ������� ��������� ������������ (��� ���� �� ���������)
   */
  public String  sessionID;
  /** ������������� ������, �� ������� ���������� �������. �� ����� �������������� �����
   * ������������ � ������������� ������������ */
  public int     problemID;
  /**
   * ������� ���������. ������ �������� � ��������� ����� � ����������� ������ �������� ������� �������
   */
  public HashMap<String, String> problemResult;
}
