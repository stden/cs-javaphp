package ru.ipo.dces.clientservercommunication;

/**
 * Запрос: удалить клиентский Plugin с сайта
 */
public class RemoveClientPluginRequest implements Request {
  public String sessionID;
  public String pluginAlias;
}
