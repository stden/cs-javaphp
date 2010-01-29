package ru.ipo.dces.clientservercommunication;

/**
 * Запрос на скачивание плагина стороны клиента
 */
public class DownloadPluginRequest implements Request {
  /**
   * Сессия суперадминистратора необходима при скачивании сервер плагина, иначе сессия не нужна
   */
  @PHPDefaultValue("null")
  public String sessionID;

  /**
   * Идентификатор плагина, который требуется установить
   */
  @PHPDefaultValue("'Test client plugin'")
  public String pluginAlias;

  @PHPDefaultValue("'Client'")
  public PluginSide side;
}
