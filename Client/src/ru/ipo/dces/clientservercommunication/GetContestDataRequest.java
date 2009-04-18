package ru.ipo.dces.clientservercommunication;

/**
 * ������: �������� ������ � ��������
 */
public class GetContestDataRequest implements Request {
  
  public enum InformationType {
    NoInfo,          //�� �������� ������ ������� ������
    ParticipantInfo, //�������� ������� ������ ��� ������������ ���������
    AdminInfo,       //�������� ������, ����������� ������� � ����� � ������
  }

  /**
   * null for anonymous user
   */
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
