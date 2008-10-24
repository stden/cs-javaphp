package ru.ipo.dces.pluginapi;

import ru.ipo.dces.clientservercommunication.*;

/**
 * ���������, ����� ������� Plugin'� ���������� � �������� ����� �������.
 * ��������� Plguin'�, ������� ������������� ������ Plugin'�
 */
public interface PluginEnvironment {

  // ��������� ������� (�� ������) ����� ����������� ����� � �� ��������
  // ������� �������. ���� ������ ������� ������� ������ ��������� �������, �
  // api ���� ����� �������� ������

  /** ���������� ����� �� ������ ������������� Plugin'� */
  public void setTitle(String title);

  /**
   * Sends information to the server-side plugin
   * 
   * @param solution
   *          information to send
   * @return recieved information. May return RequestFailedResponse
   * @throws RequestFailedResponse
   */
  public Response submitSolution(Request solution) throws RequestFailedResponse;

}
