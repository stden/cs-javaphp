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

  /** ���������� ����� �� ������ ������������� Plugin'�
   * @param title the title to set*/
  public void setTitle(String title);

  /**
   * Sends information to the server-side plugin
   * 
   * @param solution
   *          information to send
   * @return recieved information. May return RequestFailedResponse
   * @throws ru.ipo.dces.clientservercommunication.ServerReturnedError if server failed to process the problem solution,
   *          e.g. the contest is over or its problemset has changed and there is no more such problem 
   * @throws ru.ipo.dces.clientservercommunication.ServerReturnedNoAnswer if the serever is inaccessible
   */
  public Response submitSolution(Request solution) throws ServerReturnedError, ServerReturnedNoAnswer;

}
