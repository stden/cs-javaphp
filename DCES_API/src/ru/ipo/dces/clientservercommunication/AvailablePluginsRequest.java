package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: Ilya
 * Date: 14.11.2009
 * Time: 2:33:33
 *
 * ������ �� ������ �������� ������� ������� ��� ������� �������
 */
public class AvailablePluginsRequest implements Request {  

  @PHPDefaultValue("null")
  public String sessionID;

  /**
   * �����, ����� ������� ������ �� �������
   */
  @PHPDefaultValue("'Client'")
  public PluginSide pluginSide;

}
