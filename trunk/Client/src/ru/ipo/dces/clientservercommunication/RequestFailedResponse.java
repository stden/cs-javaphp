package ru.ipo.dces.clientservercommunication;

/**
 * �����: ������ ��������� ��������
 */
@SuppressWarnings("serial")
public class RequestFailedResponse extends Throwable implements Response {
  /**
   * 0 - ������� ��� �� �������, ���� ��������� �����������. 1 - ������� ���
   * ��������, ��� ������� ������������. 2 - ����� ��� ������ �� �������������,
   * ��� �����������. � �.�. � �.�.
   */
  public int    failReason;
  /**
   * � ��������� ����� ���� �������������� ���������� �� ������. ��������, ���
   * ����������� ����� ����� �� �������
   */
  public String message;

  public RequestFailedResponse(String message) {
    this.message = message;
  }
}
