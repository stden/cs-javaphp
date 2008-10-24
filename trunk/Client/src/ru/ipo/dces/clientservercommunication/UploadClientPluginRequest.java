package ru.ipo.dces.clientservercommunication;

import ru.ipo.dces.client.Controller;

/**
 * Запрос: закачать клиент на сервер
 */
public class UploadClientPluginRequest implements Request {
  public String sessionID;
  public String pluginID;
  public byte[] pluginInstaller;

  public UploadClientPluginRequest() {
    sessionID = Controller.sessionID;
  }
}
