package ru.ipo.dces.clientservercommunication;

/**
 * Запрос на скачивание плагина стороны клиента
 */
public class InstallClientPluginRequest implements Request {
  /**
   * Идентификатор плагина, который требуется установить
   */
  public String clientPluginAlias;
}
