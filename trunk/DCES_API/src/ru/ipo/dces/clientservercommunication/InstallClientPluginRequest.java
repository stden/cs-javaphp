package ru.ipo.dces.clientservercommunication;

/**
 * ������ �� ���������� ������� ������� �������
 */
public class InstallClientPluginRequest implements Request {
  /**
   * ������������� �������, ������� ��������� ����������
   */
  @PHPDefaultValue("'Test client plugin'")
  public String clientPluginAlias;
}
