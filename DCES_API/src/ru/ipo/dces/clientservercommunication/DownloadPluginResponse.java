package ru.ipo.dces.clientservercommunication;

/**
 * Ответ с плагином стороны клиента
 */
public class DownloadPluginResponse implements Response {
  /** Содержимое плагина */
  public byte[] pluginBytes;
}
