package ru.ipo.dces.clientservercommunication;

import ru.ipo.dces.client.Controller;

/**
 * ������: �������� ������ �� ������
 */
public class UploadClientPluginRequest implements Request {
  public String sessionID;
  public String pluginAlias;
  public byte[] pluginInstaller;
}
