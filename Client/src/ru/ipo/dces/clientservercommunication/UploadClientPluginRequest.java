package ru.ipo.dces.clientservercommunication;

/**
 * Запрос: закачать клиент на сервер
 */
public class UploadClientPluginRequest implements Request {
  public String sessionID;
  public String pluginID;
  public byte[] pluginInstaller;
}
