package ru.ipo.dces.clientservercommunication;

import ru.ipo.dces.client.Controller;

/**
 * ������: �������� ������ � ��������
 */
public class GetContestDataRequest implements Request {
  public String sessionID;

  /**
   * ID ��������, �����������, ���������� � sessionID, �� Super Admin ������ ���� �������� ������ ������ �� ������ ��������
   */
  public String contestID;

  /**
   * ��� ����� � ����� id ��������� �������� ����������� ������ - ������ ��� ���������������
   */
  public int[] extendedData;

  public GetContestDataRequest() {
    sessionID = Controller.sessionID;
  }
}
