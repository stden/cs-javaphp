package ru.ipo.dces.client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.jdesktop.application.Application;

import ru.ipo.dces.mock.MockServer;
import ru.ipo.dces.pluginapi.Plugin;

public class ClientDialog extends JFrame {

  public class OpenPanelAction implements ActionListener {
    private final JPanel panel;

    public OpenPanelAction(JPanel panel) {
      this.panel = panel;
    }

    public void actionPerformed(ActionEvent evt) {
      splitPane.add(panel, JSplitPane.RIGHT);
    }
  }

  private static final long serialVersionUID = -5765060013197859310L;

  /**
   * Запуск диалога в тестовом режиме
   * 
   * @param args
   */
  public static void main(String[] args) {
    MockServer ms = new MockServer();
    try {
      ms.genMockData();
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка",
          JOptionPane.ERROR_MESSAGE);
    }
    ClientData.server = ms;
    JFrame frame = new ClientDialog();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    // inst.setVisible(true);
  }

  private JSplitPane splitPane;
  public AdminPanel  adminPanel;
  private JPanel     leftPanel;
  private JButton    adminPluginButton;

  public ClientDialog() {
    super();
    initGUI();
  }

  private void initGUI() {
    try {
      BoxLayout thisLayout = new BoxLayout(getContentPane(),
          javax.swing.BoxLayout.X_AXIS);
      getContentPane().setLayout(thisLayout);
      {
        splitPane = new JSplitPane();
        getContentPane().add(splitPane);
        {
          leftPanel = new JPanel();
          leftPanel.setName("Left panel");
          GridLayout bLayout = new GridLayout(10, 1);
          bLayout.setColumns(1);
          bLayout.setHgap(2);
          bLayout.setVgap(2);
          leftPanel.setLayout(bLayout);
          splitPane.add(leftPanel, JSplitPane.LEFT);
          {
            PluginEnvironmentImpl adminEnv = new PluginEnvironmentImpl();
            adminEnv.setButton(adminPluginButton);
            adminPanel = new AdminPanel(adminEnv);
            splitPane.add(adminPanel, JSplitPane.RIGHT);
            adminPluginButton = new JButton();

            adminPluginButton.setText("User Panel");
            adminPluginButton
                .addActionListener(new OpenPanelAction(adminPanel));
            leftPanel.add(adminPluginButton, BorderLayout.NORTH);

            JButton panelButton = new JButton();
            PluginEnvironmentImpl pe = new PluginEnvironmentImpl();
            pe.setButton(panelButton);
            Plugin p = PluginLoader.load("SamplePlugin", pe);
            panelButton.addActionListener(new OpenPanelAction(p));
            leftPanel.add(panelButton, BorderLayout.NORTH);
          }
        }
      }
      setSize(800, 300);
      // Разместить окно по центру экрана
      setLocationRelativeTo(null);
      setTitle("DCES Client");

      Application.getInstance().getContext().getResourceMap(getClass())
          .injectComponents(getContentPane());
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
