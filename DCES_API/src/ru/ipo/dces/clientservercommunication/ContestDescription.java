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
   * ����� ������ �� ���������, ������� ���������� ����������
   */
  public UserDataField[]  data;

  //temporary, instead of userdata
//  public String[] data;
//  public boolean[] compulsory;

  /**
   * ��������� ������ ������� � �����������
   */
  public ResultsAccessPolicy resultsAccessPolicy;

  /**
   * ���������� �������� �������
   */
  public ContestTiming contestTiming;

}
