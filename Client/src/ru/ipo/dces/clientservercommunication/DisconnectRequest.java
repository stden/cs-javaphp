package ru.ipo.dces.clientservercommunication;

/**
 * ������: ����������� �� �������
 */
public class DisconnectRequest implements Request {

  /** ������ ������������, ������� ����� ���������� �� ������� */
  public String sessionID;

  public DisconnectRequest(String sessionID) {
    this.sessionID = sessionID;
  }
}
