package ru.ipo.dces.clientservercommunication;

/**
 * ����� �� ������ � ������ ������������ AvailableContestsRequest.
 */
public class AvailableContestsResponse implements Response {

  /**
   * ������ � ���������� ���� ������������������ � ������� �������������
   */
  @PHPDefaultValue("array()")
  public ContestDescription[] contests;

}
