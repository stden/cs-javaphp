package ru.ipo.dces.clientservercommunication;

/**
 * �����: ������ ��������� ��������
 */
@SuppressWarnings("serial")
public class RequestFailedResponse implements Response {

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
    this.message = message;
  }

  public RequestFailedResponse() {    
  }
}