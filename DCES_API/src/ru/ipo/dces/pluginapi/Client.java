package ru.ipo.dces.pluginapi;

import ru.ipo.dces.clientservercommunication.Request;
import ru.ipo.dces.clientservercommunication.Response;

/**
 * Интерфейс, через который Plugin'ы обращаются к основной части клиента
 */
public interface Client {

  // Системные плагины (не задачи) можно реализовать самим и не отдавать
  // третьей стороне. Если третья сторона захочет писать системные плагины, в
  // api надо будет добавить методы

  /**
   * Sends information to the server-side plugin
   * 
   * @param solution
   *          information to send
   * @return recieved information. May return RequestFailedResponse
   */
  public Response submitSolution(Request solution);

}
