package ru.ipo.dces.clientservercommunication;

import java.util.HashMap;

/**
 * ������: �������� ������� �� ������
 */
public class SubmitSolutionRequest implements Request {
  public String  sessionID;
  /** ID ��������, �� �������� ���������� �������. ���������� � session ID,
   *  �� super admin �������� �������� ������� ��� ������ ��������
   */
  public int     contestID;
  /** ID ������, �� ������� ���������� ������� */
  public int     problemID;
  public HashMap<String, String> problemResult;
}
