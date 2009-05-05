package ru.ipo.dces.pluginapi;

import ru.ipo.dces.exceptions.GeneralRequestFailureException;
import ru.ipo.dces.exceptions.ServerReturnedError;
import ru.ipo.dces.log.LogMessageType;

import java.util.HashMap;
import java.io.File;

/**
 * Интерфейс, через который Plugin'ы обращаются к основной части клиента.
 * Окружение Plguin'а, которое соответствует одному Plugin'у
 */
public interface PluginEnvironment {   

  // Системные плагины (не задачи) можно реализовать самим и не отдавать
  // третьей стороне. Если третья сторона захочет писать системные плагины, в
  // api надо будет добавить методы

  /** Установить текст на кнопке определенного Plugin'а
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
