package ru.ipo.dces.pluginapi;

import javax.swing.JPanel;

/**
 * Интерфейс, который должны реализовывать все Plugin'ы: системные и задачи
 * Каждый Plugin соответствует панели.
 */
public abstract class Plugin extends JPanel {

  private Client client = null;

  /** Инициализация plugin'а */
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
