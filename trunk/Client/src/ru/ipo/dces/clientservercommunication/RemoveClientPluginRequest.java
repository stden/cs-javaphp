package ru.ipo.dces.clientservercommunication;

import ru.ipo.dces.client.Controller;

/**
 * ������: ������� ���������� Plugin � �����
 */
public class RemoveClientPluginRequest implements Request {
  public String sessionID;
  public String pluginID;

  public RemoveClientPluginRequest() {
    sessionID = Controller.sessionID;
  }
}
