package ru.ipo.dces.clientservercommunication;

import java.util.Date;

/**
 * �������� ��������
 */
public class ContestDescription {
  //TODO: ��������, �� �������� �� int �� Integer, ����� �������� ���������� �������� ����� null, � �� -1 

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

  //�������� ������, ������� ������ ���� � ������� ��������� ��������. ��������, {'���','�������','�����','�����'} 
  public String[] data;

  public ContestDescription(String name) {
    this.name = name;
  }

}
