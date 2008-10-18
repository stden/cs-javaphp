package ru.ipo.dces.pluginapi;

import ru.ipo.dces.clientservercommunication.*;

/**
 * ���������, ����� ������� Plugin'� ���������� � �������� ����� �������. ������
 * ������������� ������ ����������� ����������.
 */
public interface Client {

  // ��������� ������� (�� ������) ����� ����������� ����� � �� ��������
  // ������� �������. ���� ������ ������� ������� ������ ��������� �������, �
  // api ���� ����� �������� ������

  /** ���������� ����� �� ������ ������������� Plugin'� */
  public void setTitle(Plugin plugin, String title);

  /**
   * Sends information to the server-side plugin
   * 
   * @param solution
   *          information to send
   * @return recieved information. May return RequestFailedResponse
   */
  public Response submitSolution(Request solution);

}
