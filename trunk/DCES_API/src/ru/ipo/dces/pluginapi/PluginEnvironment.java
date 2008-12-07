package ru.ipo.dces.pluginapi;

import ru.ipo.dces.clientservercommunication.*;

import java.util.HashMap;
import java.io.File;
import java.io.IOException;

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
  public HashMap<String, String> submitSolution(HashMap<String, String> solution) throws ServerReturnedError, ServerReturnedNoAnswer;


  /**
   * Get folder that contains data to create problem statement 
   * @return file, representing a directory of a problem
   */
  public File getProblemFolder();

  public String getProblemName();

}
