package ru.ipo.dces.clientservercommunication;

/**
 * �����: ������ ��������� ��������
 */
@SuppressWarnings("serial")
public class RequestFailedResponse extends Exception implements Response {

  enum FailReason {
    Default
  }

  public FailReason failReason = FailReason.Default;

  /**
   * � ��������� ����� ���� �������������� ���������� �� ������. ��������, ���
   * ����������� ����� ����� �� �������
   */
  public String     message;

  public RequestFailedResponse(String message) {
    super(message);
    this.message = message;
  }
}
