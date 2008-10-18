package ru.ipo.dces.pluginapi;

import javax.swing.JPanel;

/**
 * ���������, ������� ������ ������������� ��� Plugin'�: ��������� � ������
 * ������ Plugin ������������� ������.
 */
public abstract class Plugin extends JPanel {

  private PluginEnvironment env = null;

  /** ������������� plugin'� */
  public Plugin(PluginEnvironment env) {
    this.env = env;
  }

  protected PluginEnvironment getClient() {
    return env;
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
