package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: ����
 * Date: 11.01.2009
 * Time: 18:34:37
 * <p/>
 * ������ �� ��������� ���������� �� ����� �� �������������
 */
public class AdjustUserDataRequest implements Request {

  /**
   * id ������������, ���������� � ������� ��������� ��������
   */
  @PHPDefaultValue("0")
  public int userID;

  /**
   * ������������� ������. ������ ���� ��������������� �������������� ������� ��� �������������� ������������,
   * �������� ����������� ���������� ������������.
   */
  @PHPDefaultValue("null")
  public String sessionID;

  /**
   * ����� login ����� ������������, ���� null, ���� ����� ����� ������������� �� �����
   */
  @PHPDefaultValue("'test_login'")
  public String login;

  /**
   * ����� ������ ����� ������������, ���� null, ���� ����� ������ ������������� �� �����
   */
  @PHPDefaultValue("'test_password'")
  public String password;

  /**
   * ����� ��� ����� ������������, ���� null, ���� ��� �������� �� �����
   */
  @PHPDefaultValue("'Participant'")
  public UserDescription.UserType newType;

  /**
   * ����� ���������� �� ������������ (� ������������ � ����� data �� ContestDescription - �������� ������������,
   * � �������� ��������� ���������� ������������).
   * ���� ���������� �� ������������ �������� �� �����, � ���� userData ������������� null 
   */
  @PHPDefaultValue("array()")
  public String[] userData;
}
