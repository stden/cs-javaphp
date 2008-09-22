package ru.ipo.dces.clientservercommunication;

/**
 * Запрос на скачивание Plugin'а
 */
public class InstallClientPluginRequest implements Request {
  public String sessionID;
  public String clientPluginID;
}
