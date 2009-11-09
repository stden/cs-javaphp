package ru.ipo.dces.clientservercommunication;

/**
 * Запрос на скачивание плагина стороны клиента
 */
public class InstallClientPluginRequest implements Request {
  /**
   * Идентификатор плагина, который требуется установить
   */
  @PHPDefaultValue("'Test client plugin'")
  public String clientPluginAlias;
}
