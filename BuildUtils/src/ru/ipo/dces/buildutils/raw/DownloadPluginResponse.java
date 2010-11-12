package ru.ipo.dces.buildutils.raw;

/**
 * Ответ с плагином стороны клиента
 */
public class DownloadPluginResponse implements Response {
  /** Содержимое плагина */
  public byte[] pluginBytes;
}
