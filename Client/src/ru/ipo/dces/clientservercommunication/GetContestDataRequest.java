package ru.ipo.dces.clientservercommunication;

import ru.ipo.dces.client.ClientData;

/**
 * ������: �������� ������ � ��������
 */
public class GetContestDataRequest implements Request {
  public String sessionID;

  public GetContestDataRequest() {
    sessionID = ClientData.sessionID;
  }
}
