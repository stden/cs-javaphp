package ru.ipo.dces.clientservercommunication;

/**
 * ������ �� ����������� �� ������������
 */
public class DisconnectRequest implements Request {

  /** ������ ������������, ������� ����� ���������� �� ������� */
  @PHPDefaultValue("null")
  public String sessionID;

}
