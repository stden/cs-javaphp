package ru.ipo.dces.clientservercommunication;

import java.util.Date;

/**
 * �������� ������������. �� �������� ��������, ������ ��� ��������� ����� � ��������� ������� � ������ �������.
 */
public class ContestDescription {

  /**
   * ��� ����������� �� ������������. ���������������� ����� ���� ��������������, ���� ������ ����� ��������������
   * ������������ (�������)
   */
  public static enum RegistrationType {
    /** ����� ���������������� ������ � ������� ������� RegisterToContestRequest */
    Self,
    /** ����������� ������������� ������ �������������� */
    ByAdmins,    
  }

  /** ID ������������ */
  @PHPDefaultValue("-1")
  public int              contestID;

  /** �������� ������������ */
  @PHPDefaultValue("'Sample contest'")
  public String           name;

  /** ����������� �������� ������������ */
  @PHPDefaultValue("'Description of sample contest'")
  public String           description;

  /** ����� ������ ������������. �� ����� ������� ����������� ���������� � ������������
   * ��������� ���������� */
  @PHPDefaultValue("time()")
  public Date             start;

  /** ����� ��������� ������������. ����� ����� ������� ��������� ����� ������������ � ������������,
   * �� ������� ������� ����� ����������. (��� �������� ������� ���������� ������� SubmitSolutinRequest, �������
   * ��� ��������� �������� ������� ������� ���������� �������� ��� "�������") */
  @PHPDefaultValue("time() + 3600")
  public Date             finish;

  /** ������ ����������� �� ������������ */
  @PHPDefaultValue("'ByAdmins'")
  public RegistrationType registrationType;

  /**
   * ����� ������ �� ���������
   */
  @PHPDefaultValue("array()")
  public UserDataField[]  data;

  /**
   * ��������� ���� ������� � ����������� ������������. ���������, �����
   * ���������� � ��������� ������������� �������� ������ � ����������� ������������.
   * �������������� ������ ����� ������ � �����������
   */
  @PHPDefaultValue("")
  public ResultsAccessPolicy resultsAccessPolicy;

  /**
   * ������ ��������� ������� ���������� ������������
   */
  @PHPDefaultValue("")
  public ContestTiming contestTiming;     

}
