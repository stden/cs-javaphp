package ru.ipo.dces.clientservercommunication;

import java.util.HashMap;

/**
 * ������: �������� ������� �� ������
 */
public class SubmitSolutionRequest implements Request {
  public String  sessionID;
  /** ID ������, �� ������� ���������� ������� */
  public int     problemID; /*�������� � ���� ������ contestID*/
  public HashMap<String, String> problemResult;
}
