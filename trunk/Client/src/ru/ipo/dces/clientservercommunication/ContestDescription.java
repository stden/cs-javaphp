package ru.ipo.dces.clientservercommunication;

import java.util.Date;

/**
 * �������� ��������
 */
public class ContestDescription {

  public static enum RegistartionType {
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
  public RegistartionType registrationType;

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

  public ContestDescription() {
  };

  public ContestDescription(String name) {
    this.name = name;
  }

}
