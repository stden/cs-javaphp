package ru.ipo.dces.clientservercommunication;

/**
 * ������: ������� ���������� Plugin � �����
 */
public class RemoveClientPluginRequest implements Request {
  public String sessionID;
  public String pluginAlias;
}
