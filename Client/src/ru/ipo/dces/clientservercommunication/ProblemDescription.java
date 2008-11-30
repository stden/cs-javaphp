package ru.ipo.dces.clientservercommunication;

/**
 * �������� ������
 */
public class ProblemDescription implements Comparable<ProblemDescription> {

  /** ������������� ������, ����� ������ � ��������*/
  public int    id;

  /** ID �������, ������� ����� ������������ ������ */
  public String clientPluginID;

  /** ID �������, ������� ������� �������, �������� ���������� �� ������ */
  public String serverPluginID;

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

  @Override
  public int compareTo(ProblemDescription o) {
    return name.compareTo(o.name);
  }

}
