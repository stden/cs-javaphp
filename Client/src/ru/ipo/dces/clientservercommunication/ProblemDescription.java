package ru.ipo.dces.clientservercommunication;

/**
 * �������� ������
 */
public class ProblemDescription implements Comparable<ProblemDescription> {

  /** ������������� ������ */
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
  public byte[] problemData;

  /** ID �������� */
  public int    contestID;

  @Override
  public int compareTo(ProblemDescription o) {
    return name.compareTo(o.name);
  }

}