package ru.ipo.dces.pluginapi;

import ru.ipo.dces.exceptions.GeneralRequestFailureException;
import ru.ipo.dces.exceptions.ServerReturnedError;
import ru.ipo.dces.log.LogMessageType;

import java.util.HashMap;
import java.io.File;

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
   * @throws ru.ipo.dces.exceptions.GeneralRequestFailureException if the serever is inaccessible
   */
  public HashMap<String, String> submitSolution(HashMap<String, String> solution) throws GeneralRequestFailureException;


  /**
   * Get folder that contains data to create problem statement 
   * @return file, representing a directory of a problem
   */
  public File getProblemFolder();

  public String getProblemName();

  public void log(String message, LogMessageType type);

}
