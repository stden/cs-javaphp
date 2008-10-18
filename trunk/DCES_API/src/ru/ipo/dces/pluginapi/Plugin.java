package ru.ipo.dces.pluginapi;

import javax.swing.JPanel;

/**
 * ���������, ������� ������ ������������� ��� Plugin'�: ��������� � ������
 * ������ Plugin ������������� ������.
 */
public abstract class Plugin extends JPanel {

  private PluginEnvironment client = null;

  /** ������������� plugin'� */
  public Plugin(PluginEnvironment client) {
    this.client = client;
  }

  protected PluginEnvironment getClient() {
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
