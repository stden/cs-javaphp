package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: ����������
 * Date: 23.03.2009
 * Time: 19:35:39
 *
 * �� �������� �������� ��� �������, �� ������������ ������ ���. ����� ��������� ���� ���� ������ ��
 * ���������
 */
public class UserDataField {

  /**
   * ��� ����. ��������, "�����" ��� "�����" ��� "e-mail"
   */
  @PHPDefaultValue("name")
  public String data;

  /**
   * ����������� �� ���� ������ ���� ���������
   */
  @PHPDefaultValue("true")
  public boolean compulsory;

  /**
   * ���������� �� ���� � ������������� ����������� ������������. ��������, ��� ��������� ����������
   * � ����������� �������, � ��� ����� - ���
   */
  @PHPDefaultValue("true")
  public boolean showInResult;

}