package ru.ipo.dces.clientservercommunication;

/**
 * ������: ������� ������������
 */
public class RemoveContestRequest implements Request {

  /**
   * ������������� ������ �������������� �������
   */
  public String sessionID;

  /**
   * ������������� ���������� ������������
   */
  public int contestID;
}