package ru.ipo.dces.pluginapi;

import ru.ipo.dces.clientservercommunication.*;

/**
 * ���������, ����� ������� Plugin'� ���������� � �������� ����� �������
 */
public interface Client {

  // ��������� ������� (�� ������) ����� ����������� ����� � �� ��������
  // ������� �������. ���� ������ ������� ������� ������ ��������� �������, �
  // api ���� ����� �������� ������

  /**
   * Sends information to the server-side plugin
   * 
   * @param solution
   *          information to send
   * @return recieved information. May return RequestFailedResponse
   */
  public SubmitSolutionResponse submitSolution(SubmitSolutionRequest solution);

}
