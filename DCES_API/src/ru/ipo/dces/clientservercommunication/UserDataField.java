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
  public String data;

  /**
   * ����������� �� ���� ������ ���� ���������
   */
  public boolean compulsory;

  /**
   * ���������� �� ���� � ������������� ����������� ������������. ��������, ��� ��������� ����������
   * � ����������� �������, � ��� ����� - ���
   */
  public boolean showInResult;

}