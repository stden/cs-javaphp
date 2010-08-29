package ru.ipo.dces.clientservercommunication;

/**
 * ������: �������� ������ ������ � ������������
 */
public class GetContestDataRequest implements Request {

  /**
   * ��� ����������, ������� ��������� �������� ��� �������
   */
  public enum InformationType {
    /**
     * ������ �� ������� �������� �� ���������
     */
    NoInfo,
    /**
     * ������� ������� ����� ��� ���������, ������������ ������
     */
    ParticipantInfo,
    /**
     * ������� ������, �������������� ��� �������� ������� � ������� � ������. ���� ��� ���������� �����
     * �������� ������ ������������� ������������ ��� �������
     */
    AdminInfo,
  }

  /**
   * ������������� ������, ����� ���� null, ���� ������ ����������� ��������
   */
  @PHPDefaultValue("null")
  public String sessionID;

  /**
   * ID ������������, �� �������� ������������ ������. �������� ��� �������������� ������� � ��� �������.
   * �������� ������������ � ������������ ������������ ������ ������� -1 ��� id ������ ������������
   */
  @PHPDefaultValue("null")
  public int contestID;

  /**
   * ��� ������������� ����������
   */
  @PHPDefaultValue("'ParticipantInfo'")
  public InformationType infoType; 

  /**
   * ��� ����� � ����� id ��������� �������� ����������� ������. null - ������ ������ ��� ���� �����
   * ���� ������ �� �����, information type ��������� ������� ��� NoInfo
   */
   @PHPDefaultValue("array()")
   public int[] extendedData;                                                                                             
}
