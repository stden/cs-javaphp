package ru.ipo.dces.client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.jdesktop.application.Application;

import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.plugins.admin.LoginPluginV2;

public class ClientDialog extends JFrame {

  public class OpenPanelAction implements ActionListener {
    private final JPanel panel;

    public OpenPanelAction(JPanel panel) {
      this.panel = panel;
    }

    public void actionPerformed(ActionEvent evt) {
      rightPanel = panel;
      splitPane.add(panel, JSplitPane.RIGHT);
    }
  }

  private static final long serialVersionUID = -5765060013197859310L;

  private JSplitPane        splitPane;
  public LoginPluginV2 adminPanel;
  private JPanel            leftPanel        = null;
  private JPanel            rightPanel       = null;

  public ClientDialog() {
    super();
    initGUI();
  }

  void addPluginToForm(PluginEnvironmentImpl pe, Plugin p) {
    pe.getButton().addActionListener(new OpenPanelAction(p));
    leftPanel.add(pe.getButton(), BorderLayout.NORTH);
    // Показываем сразу первый Plugin
    if (rightPanel == null)
      setRightPanel(p);
  }

  /** Удалить все Plugin'ы */
  public void clearLeftPanel() {
    leftPanel.removeAll();
    setRightPanel(new JPanel());
    rightPanel = null;
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

          initialState();
        }
      }
      setSize(800, 300);
      // Разместить окно по центру экрана
      setLocationRelativeTo(null);
      setTitle("DCES Client");

      Application.getInstance().getContext().getResourceMap(getClass())
          .injectComponents(getContentPane());
    } catch (Exception e) {
      //TODO Show message
      System.out.println("exception in the init gui method");
      e.printStackTrace();
    }

  }

  /** Начальное состояние клиента до присоединения контеста */
  public void initialState() {
    clearLeftPanel();

    PluginEnvironmentImpl pe = new PluginEnvironmentImpl(null);
    addPluginToForm(pe, new LoginPluginV2(pe));

    // addPlugin("SamplePlugin");
  }

  private void setRightPanel(JPanel panel) {
    rightPanel = panel;
    splitPane.add(panel, JSplitPane.RIGHT);
  }
}
