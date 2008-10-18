package ru.ipo.dces.client;

import javax.swing.*;

import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.pluginapi.PluginEnvironment;

public class PluginEnvironmentImpl implements PluginEnvironment {

  private JButton button;

  public JButton getButton() {
    return button;
  }

  public void setButton(JButton button) {
    this.button = button;
  }

  @Override
  public void setTitle(String title) {
    button.setText(title);
  }

  @Override
  public Response submitSolution(Request solution) {
    JOptionPane.showMessageDialog(null, solution.toString(), "Сообщение",
        JOptionPane.INFORMATION_MESSAGE);
    return null;
  }

}
