package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: ����������
 * Date: 04.05.2009
 * Time: 1:38:00
 *
 * ������ �� ��������� ��� �������� ������� ������� �������
 */
public class AdjustServerPluginRequest implements Request {

  /**
   * ������������� ������. ��������� ������ �������������� �������
   */
  @PHPDefaultValue("null")
  public String sessionID;

  /**
   * ���������� ������������� �������. ���� ������������� ��� ��������������� � �������, ������ ����� �������.
   * � ��������� ������ ��������� ����� ������
   */
  @PHPDefaultValue("'Test server plugin'")
  public String pluginAlias;

  /**
   * ���������� php ����� � ��������. ��� ��������� ������������� ������� ����� ���� null, ��� ��������,
   * ��� ���������� �������� �� ����. ��� �������� ������ ������� ����������� ������ ���� �� null
   */
  @PHPDefaultValue("'jar file content'")
  public byte[] pluginData;

  /**
   * �������� �������. ��� ��������� ������������� ������� ����� ���� null, ��� ��������,
   * ��� ���������� �������� �� ����. ��� �������� ������ ������� ����������� ������ ���� �� null
   */
  @PHPDefaultValue("'Test server plugin description'")
  public String description;

}

