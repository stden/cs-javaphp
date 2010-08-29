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
  @PHPDefaultValue("null")
  public int      userID;

  /**
   * �����
   */
  @PHPDefaultValue("'test_login'")
  public String   login;

  /**
   * ������. � ������� ������� ��� ���� �� �����������
   */
  @PHPDefaultValue("'test_password'")
  public String   password;

  /**
   * ������ �� ���������. ���, �����, ����� � ��� ��� ������. ������ �������
   * �������� ������� �������� � �������� ��������. ContestDescription ��������
   * UserDataField[] data c ����������� � ����� � �������
   */
  @PHPDefaultValue("array()")
  public String[] dataValue;

  /** ��� ������������ */
  //TODO ���������� �� ��������������
  @PHPDefaultValue("'Participant'")
  public UserType userType = UserType.Participant;

}
