package ru.ipo.dces.clientservercommunication;

/**
 * ���������� � ������������
 */
public class UserDescription {

  public String   login;
  public String   password;

  /**
   * ������ �� ���������. ���, �����, ����� � ��� ��� ������. ������ �������
   * �������� ������� �������� � �������� ��������. ContestDescription ��������
   * String[] data c ����������� ���� {'���','�������','�����','�����'}
   */
  public String[] dataValue;

  /** �������� �� ��������������� */
  public boolean  isAdmin = false;

}
