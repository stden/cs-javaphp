package ru.ipo.dces.clientservercommunication;

import ru.ipo.dces.client.Controller;

/**
 * ������: ������� ���������� Plugin � �����
 */
public class RemoveClientPluginRequest implements Request {
  public String sessionID;
  public String pluginAlias;
}
