package ru.ipo.dces.clientservercommunication;

/**
 * ѕолучить архив с Plugin'ом
 */
public class InstallClientPluginResponse implements Response {
  /** архив с содержимым плагина */
  public byte[] pluginInstaller;
}
