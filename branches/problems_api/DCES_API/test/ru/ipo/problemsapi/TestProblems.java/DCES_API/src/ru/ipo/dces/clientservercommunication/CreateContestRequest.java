package ru.ipo.dces.clientservercommunication;

/**
 * ������ �� �������� ������ ������������
 */
public class CreateContestRequest implements Request {

  /**
   * ������������� ������. �������� ������ ������ �������������� �������
   */
  @PHPDefaultValue("null")
  public String             sessionID;

  /**
   * �������� ������������ ������������. �������� contest.contestID ������������, ��������� ���� �����������
   * ������ ���� ���������, �.�. �� ����������� ������������ null � �������� ��������
   */

  @PHPDefaultValue("")
  public ContestDescription contest;

}
