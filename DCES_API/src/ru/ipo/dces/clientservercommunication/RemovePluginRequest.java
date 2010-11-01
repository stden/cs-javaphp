package ru.ipo.dces.clientservercommunication;

/**
 * Запрос: удалить плагин стороны клиента из системы
 */
public class RemovePluginRequest implements Request {
  /**
   * Идентификатор сессии администратора сервера
   */
  @PHPDefaultValue("null")
  public String sessionID;

  @PHPDefaultValue("'Client'")
  public PluginSide side;

  /**
   * Идентификатор плагина стороны клиента
   */
  @PHPDefaultValue("'Test client plugin'")
  public String pluginAlias;
}
