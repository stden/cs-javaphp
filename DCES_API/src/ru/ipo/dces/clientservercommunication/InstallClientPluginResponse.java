package ru.ipo.dces.clientservercommunication;

/**
 * Ответ с плагином стороны клиента
 */
public class InstallClientPluginResponse implements Response {
  /** Содержимое плагина стороны клиента */
  public byte[] pluginInstaller;
}
