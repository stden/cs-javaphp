package ru.ipo.dces.clientservercommunication;

/**
 * ������: ������� ������ ������� ������� �� �������
 */
public class RemoveClientPluginRequest implements Request {
  /**
   * ������������� ������ �������������� �������
   */
  public String sessionID;

  /**
   * ������������� ������� ������� �������
   */
  public String pluginAlias;
}
