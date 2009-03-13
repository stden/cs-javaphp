package ru.ipo.dces.exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: ����
 * Date: 30.10.2008
 * Time: 1:30:38
 */
public class ServerReturnedError extends Exception {

  private int errNo;
  private String extendedInfo;

  public int getErrNo() {
    return errNo;
  }

  public String getExtendedInfo() {
    return extendedInfo;
  }

  public ServerReturnedError(int errNo, String extendedInfo) {
    super(getErrorMessage(errNo));
    this.errNo = errNo;
    this.extendedInfo = extendedInfo;
  }

  private static String getErrorMessage(int errNo) {
    switch (errNo) {
      case 0: return "������������ ���� ��� ���������� ��������";
      case 1: return "������� ������������ ���������� ��� ���������� ��������";
      case 2: return "�� ������� ����� ���������� ������������";
      case 3: return "������� ������� ������";
      case 4: return "�� ������� ����� ��������� ������";
      case 5: return "�� ������� ����� ��������� ��������� ������";
      case 6: return "�� ������� ����� ��������� ���������� ������";
      case 7: return "������ � �������� �� �������� zip ������";
      case 8: return "������ � ������� �� �������� zip ������";
      case 9: return "��������� ������ �� ������ ������ � ��������";
      case 10: return "��������� ������ �� ������ ������ � �������";
      case 11: return "������ �� �������� ���������";
      case 12: return "�������, ����� ��� ������ ������� �������";
      case 13: return "���� ������ ��� ����������";
      case 14: return "�� ������� ����� ��������� �������";
      case 15: return "�� ������� ��������� ������";
      case 16: return "������ ������� ������� �������";
      case 17: return "������������ � ������ ������� ��� ���������������";
      case 18: return "������������������ ����� ���� �������������� ������ ��� �������� ��������";
      default: return "����������� ������";
    }
  }
}
