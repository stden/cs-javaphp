package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: Ilya
 * Date: 14.11.2009
 * Time: 2:50:45
 *
 * ����� �� ������ � ��������� � ������� ��������
 */
public class AvailablePluginsResponse implements Response {

  /**
   * ������ ������� ��������
   */
  @PHPDefaultValue("array")
  public String[] aliases;

  /**
   * ������ �������� ��������
   */
  public String[] descriptions;

}
