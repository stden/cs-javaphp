package ru.ipo.dces.pluginapi;

import javax.swing.JPanel;

/**
 * Интерфейс, который должны реализовывать все Plugin'ы: системные и задачи
 * Каждый Plugin соответствует панели.
 */
public abstract class Plugin extends JPanel {

  private static final long serialVersionUID = -4016036993122910146L;

  private PluginEnvironment env              = null;

  /** Инициализация plugin'а */
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
