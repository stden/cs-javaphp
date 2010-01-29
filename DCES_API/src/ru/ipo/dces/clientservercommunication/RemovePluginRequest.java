package ru.ipo.dces.clientservercommunication;

/**
 * ������: ������� ������ ������� ������� �� �������
 */
public class RemovePluginRequest implements Request {
  /**
   * ������������� ������ �������������� �������
   */
  @PHPDefaultValue("null")
  public String sessionID;

  @PHPDefaultValue("'Client'")
  public PluginSide side;

  /**
   * ������������� ������� ������� �������
   */
  @PHPDefaultValue("'Test client plugin'")
  public String pluginAlias;
}
