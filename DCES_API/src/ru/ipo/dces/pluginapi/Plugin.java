package ru.ipo.dces.pluginapi;

import javax.swing.JPanel;

/**
 * Интерфейс, который должны реализовывать все Plugin'ы: системные и задачи
 * Каждый Plugin соответствует панели.
 */
public abstract class Plugin extends JPanel {

  private static final long serialVersionUID = -4016036993122910146L;

  private PluginEnvironment env              = null;

  /** Инициализация plugin'а
   * @param env environment for the plugin
   */
  public Plugin(PluginEnvironment env) {
    this.env = env;
  }

  protected PluginEnvironment getClient() {
    return env;
  }

  /**
   * Is called when plugin is activated, i.e. when it is shown
   */
  public void activate() {
    //do nothing
  }

  /**
   * is Called when plugin is deactivated, i.e. when it is hide 
   */
  public void deactivate() {
    //do nothing
  }

}
