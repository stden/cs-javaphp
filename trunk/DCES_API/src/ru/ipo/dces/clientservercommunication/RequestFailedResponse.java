package ru.ipo.dces.clientservercommunication;

/**
 * �����: ������ ��������� ��������
 */
@SuppressWarnings("serial")
public class RequestFailedResponse extends Exception implements Response {
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
    super(message);
    this.message = message;
  }
}