package ru.ipo.dces.clientservercommunication;

import ru.ipo.dces.client.Controller;

/**
 * ������: �������� ������ � ��������
 */
public class GetContestDataRequest implements Request {
  public String sessionID;

  public GetContestDataRequest() {
    sessionID = Controller.sessionID;
  }
}
