package ru.ipo.dces.clientservercommunication;

/**
 * �������� ������. �� �������� �������� ��� �������, �� ������������ � ���
 */
public class ProblemDescription {

  /** ������������� ������*/
  @PHPDefaultValue("-1")
  public int    id;

  /** ������������� ������� ������� �������, � ������� �������� �������� ����� ������ ������ */
  @PHPDefaultValue("'Test client plugin'")
  public String clientPluginAlias;

  /** ������������� ������� ������� �������, ������� ����� ������������ ������ ��������� */
  @PHPDefaultValue("'Test server plugin'")
  public String serverPluginAlias;

  /** �������� ������ */
  @PHPDefaultValue("'Test problem'.rand(0,239239)")
  public String name;

  /**
   * ������� ������. ��� ���������� ������� ������ ��� ���������� ���������. ���� ����������� �������� � ���
   * �������, � �������� ������� ���� ������������
   */
  @PHPDefaultValue("null")
  public byte[] statement;

  /**
   * ������� ������. ��� ������, ������� ������������ ��� �������� �������.
   */
  @PHPDefaultValue("null")
  public byte[] statementData;

  /**
   * ����� � ������. ��� ������, ������� ������������ ��� �������� ������
   */
  @PHPDefaultValue("null")
  public byte[] answerData;

}
