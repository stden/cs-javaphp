package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA. User: ���� Date: 21.05.2008 Time: 3:43:41
 */
@SuppressWarnings("serial")
public class RequestFailedResponse extends Throwable implements InfoFrame {
  public int    failReason; // 0 - ������� ��� �� �������, ���� ���������
  // �����������
  // 1 - ������� ��� ��������, ��� ������� ������������
  // 2 - ����� ��� ������ �� �������������, ��� �����������. � �.�.
  // � �.�.
  public String message;   // � ��������� ����� ���� �������������� ����������
  // �� ������. ��������,
  // ��� ����������� ����� ����� �� �������
}
