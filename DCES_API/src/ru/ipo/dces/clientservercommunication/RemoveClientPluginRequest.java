package ru.ipo.dces.clientservercommunication;

/**
 * ������: ������� ������ ������� ������� �� �������
 */
public class RemoveClientPluginRequest implements Request {
  /**
   * ������������� ������ �������������� �������
   */
  @PHPDefaultValue("null")
  public String sessionID;

  /**
   * ������������� ������� ������� �������
   */
  @PHPDefaultValue("'Test client plugin'")
  public String pluginAlias;
}
