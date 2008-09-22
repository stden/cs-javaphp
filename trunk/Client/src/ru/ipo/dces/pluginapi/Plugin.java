package ru.ipo.dces.pluginapi;

import javax.swing.JPanel;

/**
 * Интерфейс, который должны реализовывать все Plugin'ы: системные и задачи
 */
public interface Plugin {

  /**
   * Returns version of the plugin
   * 
   * @return version of the plugin
   */
  int getVersion();

  /**
   * method is called when the plugin gets out of the screen
   */
  void hide();

  /**
   * this method is called when plugin is loaded
   * 
   * @param client
   *          is an interface to the host client
   * @param panel
   *          means panel for plugin to be drawn on
   */
  void initialize(Client client, JPanel panel);

  /**
   * method is called when the plugin gets on the screen
   */
  void show();
}
