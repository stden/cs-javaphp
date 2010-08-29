package ru.ipo.dces.clientservercommunication;

/**
 * ������ �� ���������� ������� ������� �������
 */
public class DownloadPluginRequest implements Request {
  /**
   * ������ ������������������� ���������� ��� ���������� ������ �������, ����� ������ �� �����
   */
  @PHPDefaultValue("null")
  public String sessionID;

  /**
   * ������������� �������, ������� ��������� ����������
   */
  @PHPDefaultValue("'Test client plugin'")
  public String pluginAlias;

  @PHPDefaultValue("'Client'")
  public PluginSide side;
}
