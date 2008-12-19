package ru.ipo.dces.pluginapi;

import javax.swing.JPanel;

/**
 * Интерфейс, который должны реализовывать все Plugin'ы: системные и задачи
 * Каждый Plugin соответствует панели.
 */
public interface Plugin {

  public JPanel getPanel();

  /**
   * Is called when plugin is activated, i.e. when it is shown
   */
  public void activate();

  /**
   * is Called when plugin is deactivated, i.e. when it is hidden
   */
  public void deactivate();

}
