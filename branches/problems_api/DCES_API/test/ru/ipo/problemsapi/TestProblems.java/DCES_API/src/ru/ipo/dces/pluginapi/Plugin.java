package ru.ipo.dces.pluginapi;

import javax.swing.JPanel;

/**
 * ���������, ������� ������ ������������� ��� Plugin'�: ��������� � ������
 * ������ Plugin ������������� ������.
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
