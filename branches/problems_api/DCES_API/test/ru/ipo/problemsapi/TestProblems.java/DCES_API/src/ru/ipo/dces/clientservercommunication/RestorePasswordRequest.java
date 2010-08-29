package ru.ipo.dces.clientservercommunication;

/**
 * ������: �������������� ������. ���� �� ����������
 */
public class RestorePasswordRequest implements Request {

  /**
   * ������������� ������������, � ������������ �������� ����������������� ������
   */
  @PHPDefaultValue("null")
  public String contestID;

  /**
   * ����� ������������, �������� ������� ��������������� ������
   */
  @PHPDefaultValue("'test_login'")
  public String login;
}
