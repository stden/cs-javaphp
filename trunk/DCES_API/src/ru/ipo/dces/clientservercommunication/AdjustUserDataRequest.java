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
  public int userID;

  /**
   * ������������� ������. ������ ���� ��������������� �������������� ������� ��� �������������� ������������,
   * �������� ����������� ���������� ������������.
   */
  public String sessionID;

  /**
   * ����� login ����� ������������, ���� null, ���� ����� ����� ������������� �� �����
   */
  public String login;

  /**
   * ����� ������ ����� ������������, ���� null, ���� ����� ������ ������������� �� �����
   */
  public String password;

  /**
   * ����� ��� ����� ������������, ���� null, ���� ��� �������� �� �����
   */
  public UserDescription.UserType newType;

  /**
   * ����� ���������� �� ������������ (� ������������ � ����� data �� ContestDescription - �������� ������������,
   * � �������� ��������� ���������� ������������).
   * ���� ���������� �� ������������ �������� �� �����, � ���� userData ������������� null 
   */
  public String[] userData;
}
