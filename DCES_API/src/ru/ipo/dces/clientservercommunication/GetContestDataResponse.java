package ru.ipo.dces.clientservercommunication;

import ru.ipo.dces.clientservercommunication.ContestDescription;

/**
 * �����: ���������� � ������������, ������ ���������� � ��������, ���� ������ ���
 * ������ ������
 */
public class GetContestDataResponse implements Response {
  /**
   * ������ �� ����������� �������
   */
  public ProblemDescription problems[];

  /**
   * �������� ������������ 
   */
  public ContestDescription contest;
}
