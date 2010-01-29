package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: ����
 * Date: 18.12.2008
 * Time: 23:30:51
 *
 * ������ �� ��������� ��� �������� ������� ������� �������
 */
public class AdjustPluginRequest implements Request {

  /**
   * ������������� ������. ��������� ������ �������������� �������
   */
  @PHPDefaultValue("null")
  public String sessionID;

  @PHPDefaultValue("Client")
  public PluginSide side;

  /**
   * ���������� ������������� �������. ���� ������������� ��� ��������������� � �������, ������ ����� �������.
   * � ��������� ������ ��������� ����� ������
   */
  @PHPDefaultValue("'Test client plugin'")
  public String pluginAlias;

  /**
   * ���������� jar ����� � ��������. ��� ��������� ������������� ������� ����� ���� null, ��� ��������,
   * ��� ���������� �������� �� ����. ��� �������� ������ ������� ����������� ������ ���� �� null
   */
  @PHPDefaultValue("'jar file content'")
  public byte[] pluginData;

  /**
   * �������� �������. ��� ��������� ������������� ������� ����� ���� null, ��� ��������,
   * ��� ���������� �������� �� ����. ��� �������� ������ ������� ����������� ������ ���� �� null
   */
  @PHPDefaultValue("'Test client plugin description'")
  public String description;

}
