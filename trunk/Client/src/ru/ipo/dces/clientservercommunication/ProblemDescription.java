package ru.ipo.dces.clientservercommunication;

/**
 * �������� ������
 */
public class ProblemDescription {
  /** ������������� ������ */
  public String id;
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

}
