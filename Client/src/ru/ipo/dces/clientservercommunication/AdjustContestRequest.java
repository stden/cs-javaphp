package ru.ipo.dces.clientservercommunication;

/**
 * �������� ������ �������� ��������
 */
public class AdjustContestRequest implements Request {

  public String               sessionID;
  public ContestDescription   contest;
  public ProblemDescription[] problems;

}
