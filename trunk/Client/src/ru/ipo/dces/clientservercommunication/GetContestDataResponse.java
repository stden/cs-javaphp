package ru.ipo.dces.clientservercommunication;

/**
 * �����: ���������� � ��������, ������ ���������� � ��������, ���� ������ ���
 * ������ ������
 */
public class GetContestDataResponse implements Response {
  public ProblemDescription problems[];
  public ContestDescription contest;
}
