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
  public FileFolder statement;

  /**
   * ������� ������. ��� ������, ������� ������������ ��� �������� �������
   */
  public FileFolder statementData;

  /**
   * ����� � ������. ��� ������, ������� ������������ ��� �������� ������
   */
  public FileFolder answerData;

  @Override
  public int compareTo(ProblemDescription o) {
    return name.compareTo(o.name);
  }

}
