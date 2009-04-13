package ru.ipo.dces.client;

import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.ArrayList;

import javax.swing.*;

import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.plugins.admin.LoginPluginV2;
import ru.ipo.dces.plugins.admin.ResultsPlugin;
import info.clearthought.layout.TableLayout;

public class ClientDialog extends JFrame {
  private static final int MIN_LEFT_PANEL_ROWS = 10;

  public class OpenPanelActionListener implements ActionListener {
    private final Plugin plugin;

    public OpenPanelActionListener(Plugin plugin) {
      this.plugin = plugin;
    }

    public void actionPerformed(ActionEvent evt) {     
      setRightPanel(plugin);
    }
  }

  private static final long serialVersionUID = -5765060013197859310L;

  private JSplitPane splitPane;
  private JPanel leftPanel = null;
  private Plugin rightPanel = null;
  private GridLayout bLayout;
  private ButtonGroup pluginsButtonGroup = new ButtonGroup();
  private JTextPane logTextPane;

  public void addPluginToForm(PluginEnvironmentView pev, Plugin p) {
    if (p == null) {
      Controller.getLogger().log("Не удалось загрузить плагин", LogMessageType.Error, Localization.LOGGER_NAME);
      return;
    }

    final JToggleButton button = pev.getButton();
    button.addActionListener(new OpenPanelActionListener(p));

    //increase left plugin size if there are
    bLayout.setRows(Math.max(MIN_LEFT_PANEL_ROWS, bLayout.getRows() + 1));

    //make button radio
    pluginsButtonGroup.add(button);

    //add button
    leftPanel.add(button, BorderLayout.NORTH);
    // Показываем сразу первый Plugin
    if (rightPanel == null) {
      setRightPanel(p);      
      button.setSelected(true);
    } else
      button.setSelected(false);
  }

  /**
   * Удалить все Plugin'ы
   */
  public void clearLeftPanel() {
    leftPanel.removeAll();
    setRightPanel(new Plugin() {
      public JPanel getPanel() {
        return new JPanel();
      }

      public void activate() {
      }

      public void deactivate() {
      }
    });
    rightPanel = null;

    bLayout.setRows(MIN_LEFT_PANEL_ROWS);

    //clear radio group of left buttons
    ArrayList<AbstractButton> al = new ArrayList<AbstractButton>();
    final Enumeration<AbstractButton> buttonEnumeration = pluginsButtonGroup.getElements();
    while (buttonEnumeration.hasMoreElements()) {
      AbstractButton b = buttonEnumeration.nextElement();
      al.add(b);
    }
    for (AbstractButton button : al) {
      pluginsButtonGroup.remove(button);
    }
  }

  public void initGUI() {
    //set outer components and outer layout
    TableLayout outerLayout = new TableLayout(new double[][]
            {{TableLayout.FILL}, {20, TableLayout.FILL, 60}}
    );
    getContentPane().setLayout(outerLayout);
    JPanel mainPanel = new JPanel();
    logTextPane = new JTextPane();
    JScrollPane logScrollPane = new JScrollPane(logTextPane);
    getContentPane().add(Controller.getContestChooserPanel(), "0, 0");
    getContentPane().add(mainPanel, "0, 1");
    getContentPane().add(logScrollPane, "0, 2");

    //logTextPane adjust
    logTextPane.setEditable(false);
    logTextPane.setBackground(Color.BLACK);

    //set mainPanel layout and components
    BoxLayout thisLayout = new BoxLayout(mainPanel, BoxLayout.X_AXIS);
    mainPanel.setLayout(thisLayout);

    splitPane = new JSplitPane();
    mainPanel.add(splitPane);

    leftPanel = new JPanel();
    leftPanel.setName("Left plugin");
    bLayout = new GridLayout(MIN_LEFT_PANEL_ROWS, 1);
    bLayout.setColumns(1);
    bLayout.setHgap(2);
    bLayout.setVgap(2);
    leftPanel.setLayout(bLayout);
    splitPane.add(leftPanel, JSplitPane.LEFT);

    initialState();

    setSize(800, 600);
    // Разместить окно по центру экрана
    setLocationRelativeTo(null);
    setTitle("DCES Client");
  }

  /**
   * Начальное состояние клиента до присоединения контеста
   */
  public void initialState() {
    clearLeftPanel();

    //add login plugin.
    //can not use Controller.addAdminPlugin() because sometimes it's called from constructor
    //so here it is some code duplication
    PluginEnvironmentImpl pe1 = new PluginEnvironmentImpl(Localization.getAdminPluginName(LoginPluginV2.class));
    addPluginToForm(pe1.getView(), new LoginPluginV2(pe1));

    PluginEnvironmentImpl pe2 = new PluginEnvironmentImpl(Localization.getAdminPluginName(ResultsPlugin.class));
    addPluginToForm(pe2.getView(), new ResultsPlugin(pe2));
  }

  private void setRightPanel(Plugin panel) {
    if (rightPanel != null) rightPanel.deactivate();
    rightPanel = panel;
    Controller.setCurrentPlugin(panel);
    splitPane.add(panel.getPanel(), JSplitPane.RIGHT);
    if (rightPanel != null) rightPanel.activate();
  }

  public JTextPane getLogTextPane() {
    return logTextPane;
  }

}
