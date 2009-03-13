package ru.ipo.dces.client;

import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.plugins.admin.LoginPluginV2;
import info.clearthought.layout.TableLayout;

public class ClientDialog extends JFrame {
  private static final int MIN_LEFT_PANEL_ROWS = 10;

  public class OpenPanelActionListener implements ActionListener {
    private final Plugin plugin;

    public OpenPanelActionListener(Plugin plugin) {
      this.plugin = plugin;
    }

    public void actionPerformed(ActionEvent evt) {
      //TODO: create a dialog of correct size
      /*Dimension preferredPluginSize = plugin.getPreferredSize();
      Dimension preferredMenuSize = leftPanel.getPreferredSize();

      int hDelta = ClientDialog.this.getHeight() - ClientDialog.this.getContentPane().getHeight();
      int wDelta = ClientDialog.this.getWidth() - ClientDialog.this.getContentPane().getWidth();

      ClientDialog.this.setSize(
              (int)(preferredPluginSize.getWidth() + preferredMenuSize.getWidth() + wDelta + 30),
              (int)Math.max(preferredPluginSize.getHeight(), preferredMenuSize.getHeight() + hDelta)
      );*/

      if (rightPanel != null) rightPanel.deactivate();
      setRightPanel(plugin);
      if (rightPanel != null) rightPanel.activate();
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
      Controller.getLogger().log("Не удалось загрузить плагин", UserMessagesLogger.LogMessageType.Error, Controller.LOGGER_NAME);
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
      p.activate();
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
            {{TableLayout.FILL}, {TableLayout.FILL, 60}}
    );
    getContentPane().setLayout(outerLayout);
    JPanel mainPanel = new JPanel();
    logTextPane = new JTextPane();
    final JScrollPane logScrollPane = new JScrollPane(logTextPane);
    getContentPane().add(mainPanel, "0, 0");
    getContentPane().add(logScrollPane, "0, 1");

    //logTextPane adjust
    logTextPane.setEditable(false);
    logTextPane.setBackground(Color.BLACK);
    logTextPane.getDocument().addDocumentListener(new DocumentListener() {
      public void insertUpdate(DocumentEvent e) {
        //scroll to the top
        logScrollPane.getVerticalScrollBar().setValue(0);
        logScrollPane.getHorizontalScrollBar().setValue(0);
      }

      public void removeUpdate(DocumentEvent e) {/*do nothing*/}
      public void changedUpdate(DocumentEvent e) {/*do nothing*/}
    });

    //set mainPanel layout and components
    BoxLayout thisLayout = new BoxLayout(mainPanel, javax.swing.BoxLayout.X_AXIS);
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
    PluginEnvironmentImpl pe = new PluginEnvironmentImpl(null);
    addPluginToForm(pe.getView(), new LoginPluginV2(pe));

    // addPlugin("SamplePlugin");
  }

  private void setRightPanel(Plugin panel) {
    rightPanel = panel;
    splitPane.add(panel.getPanel(), JSplitPane.RIGHT);
  }

  public JTextPane getLogTextPane() {
    return logTextPane;
  }
}
