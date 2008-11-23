package ru.ipo.dces.clientservercommunication;

import java.util.Date;

/**
 * �������� ��������
 */
public class ContestDescription {

  public static enum RegistrationType {
    /** ����� ���������������� ������ � ������� ������� */
    Self,
    /** ����������� ������ �������������� */
    ByAdmins,
    /** �������� �� ����������� */
    NotDefined
  }

  /** ID �������� */
  public int              contestID;

  /** �������� �������� */
  public String           name;

  /** �������� �������� */
  public String           description;

  /** ����� ������ �������� */
  public Date             start;

  /** ����� ��������� �������� */
  public Date             finish;

  /** ������ ����������� �� ������� */
  public RegistrationType registrationType;

  /**
   * �������� �� ����� ��������� �� ������ ���� ������, ��������� �����
   * ��������/���������
   */
  public boolean          visible = true;

  /**
   * �������� ������, ������� ������ ���� � ������� ��������� ��������.
   * ��������, {'���','�������','�����','�����'}
   */
  public String[]         data;

  /**
   * �������������� ���� �� ������� data 
   * */
  public boolean[]        compulsory;

  public ContestDescription() {
  }

  public ContestDescription(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }
}
