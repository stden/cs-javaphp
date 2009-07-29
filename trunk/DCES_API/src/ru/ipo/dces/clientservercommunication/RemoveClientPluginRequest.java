package ru.ipo.dces.clientservercommunication;

/**
 * Запрос: удалить плагин стороны клиента из системы
 */
public class RemoveClientPluginRequest implements Request {
  /**
   * Идентификатор сессии администратора сервера
   */
  public String sessionID;

  /**
   * Идентификатор плагина стороны клиента
   */
  public String pluginAlias;
}
