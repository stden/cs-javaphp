package ru.ipo.dces.clientservercommunication;

import java.util.Date;

/**
 * �������� ��������
 */
public class ContestDescription {

  public int    contestID;

  // �������� ��������� ���������
  public String name;

  // �������� ��������
  public String description;

  // ����� ������ ��������
  public Date   start;

  // ����� ��������� ��������
  public Date   finish;

  // �������� �������� - ������ �����������.
  // 0 - ����� ���������������� ������ � ������� �������
  // 1 - ����������� ������ ��������������
  // -1 - �������� �� �����������
  public int    registrationType;

  // 0 - ���������
  // 1 - �������
  // -1 - �������� �� �����������
  public int    visible;         // �������� �� ����� ��������� �� ������ ����

  // ������, ��������� ����� �������� ���������

  public ContestDescription(String name) {
    this.name = name;
  }

}
