package ru.ipo.dces.pluginapi;

import ru.ipo.dces.clientservercommunication.*;

/**
 * Интерфейс, через который Plugin'ы обращаются к основной части клиента.
 * Окружение Plguin'а, которое соответствует одному Plugin'у
 */
public interface PluginEnvironment {

  // Системные плагины (не задачи) можно реализовать самим и не отдавать
  // третьей стороне. Если третья сторона захочет писать системные плагины, в
  // api надо будет добавить методы

  /** Установить текст на кнопке определенного Plugin'а */
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
