package ru.ipo.dces.clientservercommunication;

/**
 * ������: ������� ������������
 */
public class RemoveContestRequest implements Request {

  /**
   * ������������� ������ �������������� �������
   */
  @PHPDefaultValue("null")
  public String sessionID;

  /**
   * ������������� ���������� ������������
   */
  @PHPDefaultValue("null")
  public int contestID;
}