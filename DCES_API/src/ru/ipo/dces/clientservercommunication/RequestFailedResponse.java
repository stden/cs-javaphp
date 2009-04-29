package ru.ipo.dces.clientservercommunication;

/**
 * �����: ������ ��������� ��������
 */
public class RequestFailedResponse implements Response {

  public enum FailReason {
    BusinessLogicError,
    BrokenServerError,
    BrokenServerPluginError,
  }

  public FailReason failReason;

  /**
   * ����� ������, ��. �������
   */
  public int        failErrNo;

  /**
   * �������������� � ������ ����������
   * ��� BuisnessLogicError - ��� ������
   * ��� BrokenServerError  - ������ �����, �������� �������� � MySQL
   * ��� BrokenServerPluginError - �������� �������
   */
  public String     extendedInfo;

}