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
      rightPanel = panel;
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
  }

  private JSplitPane splitPane;
  public LoginPlugin adminPanel;
  private JPanel     leftPanel  = null;
  private JPanel     rightPanel = null;

  public ClientDialog() {
    super();
    initGUI();
  }

  /** Добавление Plugin'а в клиент */
  void addPlugin(String plugin_id) {
    PluginEnvironmentImpl pe = createPluginEnv();

    Plugin p = PluginLoader.load(plugin_id, pe);

    addPluginToForm(pe, p);
  }

  void addPluginToForm(PluginEnvironmentImpl pe, Plugin p) {
    pe.getButton().addActionListener(new OpenPanelAction(p));
    leftPanel.add(pe.getButton(), BorderLayout.NORTH);
    // Показываем сразу первый Plugin
    if (rightPanel == null)
      setRightPanel(p);
  }

  PluginEnvironmentImpl createPluginEnv() {
    JButton panelButton = new JButton();
    PluginEnvironmentImpl pe = new PluginEnvironmentImpl();
    pe.setButton(panelButton);
    return pe;
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
      e.printStackTrace();
    }

  }

  /** Начальное состояние клиента до присоединения контеста */
  public void initialState() {
    removeAllPlugins();

    PluginEnvironmentImpl pe = createPluginEnv();
    addPluginToForm(pe, new LoginPlugin(pe, this));

    addPlugin("SamplePlugin");
  }

  /** Удалить все Plugin'ы */
  public void removeAllPlugins() {
    leftPanel.removeAll();
    setRightPanel(new JPanel());
    rightPanel = null;
  }

  private void setRightPanel(JPanel panel) {
    rightPanel = panel;
    splitPane.add(panel, JSplitPane.RIGHT);
  }
}
