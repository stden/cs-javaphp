package ru.ipo.dces.clientservercommunication;

import java.util.HashMap;

/**
 * �����: ������� ����������
 */
public class SubmitSolutionResponse implements Response {
  /** ����������� ������ � �������� ������.
   * ��� ������ ����� ���� ������������. ������ ������ �������� �����, ����� ����� �� ������� �� �������
   * (�� ������� ������� �������), � �������� ��������� � ������� ���� ������.
   */
  public HashMap<String, String> problemResult;
  // ��������, �������������� ������ � �������� ��������
}
