package ru.ipo.dces.clientservercommunication;

/**
 * ���������� � ������������. �� �������� �������� ��� �������, �� ���������� � ���
 */
public class UserDescription {

  /**
   * ��� ������������ � �������. �� ���� ������� ����� ��������� ������������ ��������
   */
  public enum UserType {
    /**
     * ��������
     */
    Participant,
    /**
     * ������������� ������������
     */
    ContestAdmin,
    /**
     * ������������� �������
     */
    SuperAdmin,
  }

  /**
   * ������������� ������������
   */
  public int      userID;

  /**
   * �����
   */
  public String   login;

  /**
   * ������. � ������� ������� ��� ���� �� �����������
   */
  public String   password;

  /**
   * ������ �� ���������. ���, �����, ����� � ��� ��� ������. ������ �������
   * �������� ������� �������� � �������� ��������. ContestDescription ��������
   * UserDataField[] data c ����������� � ����� � �������
   */
  public String[] dataValue;

  /** ��� ������������ */
  //TODO ���������� �� ��������������
  public UserType userType = UserType.Participant;

}
