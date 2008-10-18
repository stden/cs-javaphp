package ru.ipo.dces.clientservercommunication;

/**
 * ���������� � ������������
 */
public class UserDescription {

  public enum UserType {
    User, ContestAdmin, SuperAdmin
  }

  public String   login;
  public String   password;

  /**
   * ������ �� ���������. ���, �����, ����� � ��� ��� ������. ������ �������
   * �������� ������� �������� � �������� ��������. ContestDescription ��������
   * String[] data c ����������� ���� {'���','�������','�����','�����'}
   */
  public String[] dataValue;

  /** ��� ������������ */
  public UserType userType = UserType.User;

}
