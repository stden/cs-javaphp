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
  HashMap<String, String> problemResult;
  // ��������, �������������� ������ � �������� ��������
}
