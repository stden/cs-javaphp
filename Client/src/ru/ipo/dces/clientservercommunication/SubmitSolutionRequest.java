package ru.ipo.dces.clientservercommunication;

/**
 * ������: �������� ������� �� ������
 */
public class SubmitSolutionRequest implements Request {
  public String  sessionID;
  /** ID ������, �� ������� ���������� ������� */
  public String  problemID;
  public Request problemResult;
}
