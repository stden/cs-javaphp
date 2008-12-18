package ru.ipo.dces.clientservercommunication;

/**
 * �������� ������
 */
public class ProblemDescription {

  /** ������������� ������, ����� ������ � ��������*/
  public int    id;

  /** ID �������, ������� ����� ������������ ������ */
  public String clientPluginAlias;

  /** ID �������, ������� ������� �������, �������� ���������� �� ������ */
  public String serverPluginAlias;

  /** �������� */
  public String name;

  /**
   * ������� ������. ��������� �����, ��� �����, ������� ����� �������������
   * ������������ � �������, ��������������� ������
   */
  public byte[] statement;

  /**
   * ������� ������. ��� ������, ������� ������������ ��� �������� �������
   */
  public byte[] statementData;

  /**
   * ����� � ������. ��� ������, ������� ������������ ��� �������� ������
   */
  public byte[] answerData;

}
