package ru.ipo.dces.clientservercommunication;

import ru.ipo.dces.client.ClientData;

/**
 * ������: ������� ���������� Plugin � �����
 */
public class RemoveClientPluginRequest implements Request {
  public String sessionID;
  public String pluginID;

  public RemoveClientPluginRequest() {
    sessionID = ClientData.sessionID;
  }
}
