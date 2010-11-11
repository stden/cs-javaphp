package ru.ipo.dces.clientservercommunication;

/**
 * Запрос: удалить плагин стороны клиента из системы
 */
public class RemovePluginRequest implements Request {
  /**
   * Идентификатор сессии администратора сервера
   */
  @BinInfo(phpDefaultValue="null")
  public String sessionID;

  @BinInfo(phpDefaultValue="'Client'")
  public PluginSide side;

  /**
   * Идентификатор плагина стороны клиента
   */
  @BinInfo(phpDefaultValue="'Test client plugin'")
  public String pluginAlias;
}
