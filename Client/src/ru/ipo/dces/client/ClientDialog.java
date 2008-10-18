package ru.ipo.dces.client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.jdesktop.application.Application;

import ru.ipo.dces.mock.MockServer;
import ru.ipo.dces.pluginapi.Plugin;

public class ClientDialog extends JDialog {

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
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        MockServer ms = new MockServer();
        try {
          ms.genData();
        } catch (Exception e) {
          JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка",
              JOptionPane.ERROR_MESSAGE);
        }
        ClientData.server = ms;
        JFrame frame = new JFrame();
        ClientDialog inst = new ClientDialog(frame);
        inst.setVisible(true);
      }
    });
  }

  private JSplitPane splitPane;
  public AdminPanel  adminPanel;
  private JPanel     leftPanel;
  private JButton    userPanelButton;

  public ClientDialog(JFrame frame) {
    super(frame);
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
        adminPanel = new AdminPanel(ClientImpl.getInstance());
        splitPane.add(adminPanel, JSplitPane.RIGHT);
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
            userPanelButton = new JButton();
            userPanelButton.setText("User Panel");
            userPanelButton.setName("User Panel");
            userPanelButton.addActionListener(new OpenPanelAction(adminPanel));
            leftPanel.add(userPanelButton, BorderLayout.NORTH);

            for (int i = 2; i < 7; i++) {
              JButton panelButton = new JButton();
              JPanel panel = new JPanel();
              String pluginName = "Plugin " + i;
              panelButton.setText(pluginName);
              panel.setName(pluginName);
              panelButton.addActionListener(new OpenPanelAction(panel));
              leftPanel.add(panelButton, BorderLayout.NORTH);
            }

            PluginLoader pl = PluginLoader.getInstance();
            Plugin p = pl.loadPlugin("SamplePlugin");
            PluginInfo pinf = new PluginInfo();
            ClientData.plugin2info.put(p, pinf);
            JButton panelButton = new JButton();
            panelButton.setText("Попытка плагина");
            panelButton.addActionListener(new OpenPanelAction(p));
            leftPanel.add(panelButton, BorderLayout.NORTH);
            pinf.setPluginButton(panelButton);
          }
        }
      }
      setSize(400, 300);
      setTitle("DCES Client");

      Application.getInstance().getContext().getResourceMap(getClass())
          .injectComponents(getContentPane());
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
