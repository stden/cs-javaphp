package ru.ipo.dces.clientservercommunication;

/**
 * ������: �������� ������ �� ������
 */
public class UploadClientPluginRequest implements Request {
  public String sessionID;
  public String pluginAlias;
  public byte[] pluginInstaller;
}
