package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: ����
 * Date: 11.12.2008
 * Time: 23:05:01
 *
 * ������ �� �������������� �������� ���� ������ �� �������. ����������� ����� � ������ ��������������
 * �������. ������ �������������� ������� ����� ���� ��������� ����� 
 */
public class CreateDataBaseRequest implements Request {

  /**
   * ����� �������������� �������
   */
  @PHPDefaultValue("admin")
  public String login;

  /**
   * ������ �������������� �������
   */
  @PHPDefaultValue("superpassword")
  public String password;

}
