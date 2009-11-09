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
  @PHPDefaultValue("null")
  public String  sessionID;

  /** ������������� ������, �� ������� ���������� �������. �� ����� �������������� �����
   * ������������ � ������������� ������������ */
  @PHPDefaultValue("null")
  public int     problemID;

  /**
   * ������� ���������. ������ �������� � ��������� ����� � ����������� ������ �������� ������� �������
   */
  @PHPDefaultValue("array()")
  public HashMap<String, String> problemResult;
}
