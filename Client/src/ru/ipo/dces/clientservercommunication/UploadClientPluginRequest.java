package ru.ipo.dces.clientservercommunication;

import ru.ipo.dces.client.ClientData;

/**
 * ������: �������� ������ �� ������
 */
public class UploadClientPluginRequest implements Request {
  public String sessionID;
  public String pluginID;
  public byte[] pluginInstaller;

  public UploadClientPluginRequest() {
    sessionID = ClientData.sessionID;
  }
}
