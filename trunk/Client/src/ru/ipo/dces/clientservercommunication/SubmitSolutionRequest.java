package ru.ipo.dces.clientservercommunication;

/**
 * ������: �������� ������� �� ������
 */
public class SubmitSolutionRequest implements Request {
  public String  sessionID;
  /** ID ������, �� ������� ���������� ������� */
  public int     problemID;
  public Request problemResult;
}
