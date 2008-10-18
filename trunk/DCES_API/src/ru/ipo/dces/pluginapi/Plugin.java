package ru.ipo.dces.pluginapi;

import javax.swing.JPanel;

/**
 * ���������, ������� ������ ������������� ��� Plugin'�: ��������� � ������
 * ������ Plugin ������������� ������.
 */
public abstract class Plugin extends JPanel {

  private Client client = null;

  /** ������������� plugin'� */
  public Plugin(Client client) {
    this.client = client;
  }

  protected Client getClient() {
    return client;
  }

  /**
   * Returns version of the plugin
   * 
   * @return version of the plugin
   */
  int getVersion() {
    return 1;
  }
}
