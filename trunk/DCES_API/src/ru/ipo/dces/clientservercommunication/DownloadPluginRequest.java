package ru.ipo.dces.clientservercommunication;

/**
 * Запрос на скачивание плагина стороны клиента
 */
public class DownloadPluginRequest implements Request {
  /**
   * Сессия суперадминистратора необходима при скачивании сервер плагина, иначе сессия не нужна
   */
  @BinInfo(phpDefaultValue="null")
  public String sessionID;

  /**
   * Идентификатор плагина, который требуется установить
   */
  @BinInfo(phpDefaultValue="'Test client plugin'")
  public String pluginAlias;

  @BinInfo(phpDefaultValue="'Client'")
  public PluginSide side;
}
