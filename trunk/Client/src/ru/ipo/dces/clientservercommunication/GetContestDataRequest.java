package ru.ipo.dces.clientservercommunication;

import ru.ipo.dces.client.Controller;

/**
 * ������: �������� ������ � ��������
 */
public class GetContestDataRequest implements Request {
  
  public enum InformationType {
    NoInfo,          //�� �������� ������ ������� ������
    ParticipantInfo, //�������� ������� ������ ��� ������������ ���������
    AdminInfo,       //�������� ������, ����������� ������� � ����� � ������
  }

  public String sessionID;

  /**
   * ID ��������. ������-�� �� ���������� � sessionID, �� Super Admin ������ ���� �������� ������ ������ �� ������ ��������
   */
  public int contestID;

  public InformationType infoType; 

  /**
   * ��� ����� � ����� id ��������� �������� ����������� ������. null - ������ ������ ��� ���� �����
   * ���� ������ �� �����, information type ��������� ������� ��� NoInfo
   */
   public int[] extendedData;                                                                                             
}
