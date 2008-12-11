package ru.ipo.dces.client;

import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.ArrayList;

import javax.swing.*;

import org.jdesktop.application.Application;

import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.plugins.admin.LoginPluginV2;

public class ClientDialog extends JFrame {
  private static final int MIN_LEFT_PANEL_ROWS = 10;

  public class OpenPanelActionListener implements ActionListener {
    private final Plugin panel;

    public OpenPanelActionListener(Plugin panel) {
      this.panel = panel;
    }

    public void actionPerformed(ActionEvent evt) {
      //TODO: create a dialog of correct size
      /*Dimension preferredPluginSize = panel.getPreferredSize();
      Dimension preferredMenuSize = leftPanel.getPreferredSize();

      int hDelta = ClientDialog.this.getHeight() - ClientDialog.this.getContentPane().getHeight();
      int wDelta = ClientDialog.this.getWidth() - ClientDialog.this.getContentPane().getWidth();

      ClientDialog.this.setSize(
              (int)(preferredPluginSize.getWidth() + preferredMenuSize.getWidth() + wDelta + 30),
              (int)Math.max(preferredPluginSize.getHeight(), preferredMenuSize.getHeight() + hDelta)
      );*/

      if (rightPanel != null) rightPanel.deactivate();
      setRightPanel(panel);
      if (rightPanel != null) rightPanel.activate();
    }
  }

  private static final long serialVersionUID = -5765060013197859310L;

  private JSplitPane        splitPane;
  public LoginPluginV2 adminPanel;
  private JPanel            leftPanel        = null;
  private Plugin            rightPanel       = null;
  private GridLayout        bLayout;
  private ButtonGroup pluginsButtonGroup = new ButtonGroup();

  public ClientDialog() {
    super();
    initGUI();
  }

  public void addPluginToForm(PluginEnvironmentView pev, Plugin p) {
    if (p == null) {
      JOptionPane.showMessageDialog(null, "Не удалось загрузить плагин");
      return;
    }

    final JToggleButton button = pev.getButton();
    button.addActionListener(new OpenPanelActionListener(p));

    //increase left panel size if there are
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
    }
    else
      button.setSelected(false);
  }

  /** Удалить все Plugin'ы */
  public void clearLeftPanel() {
    leftPanel.removeAll();
    setRightPanel(new Plugin(null) {});
    rightPanel = null;

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
          bLayout = new GridLayout(MIN_LEFT_PANEL_ROWS, 1);
          bLayout.setColumns(1);
          bLayout.setHgap(2);
          bLayout.setVgap(2);
          leftPanel.setLayout(bLayout);
          splitPane.add(leftPanel, JSplitPane.LEFT);

          initialState();
        }
      }
      setSize(800, 600);
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
    addPluginToForm(pe.getView(), new LoginPluginV2(pe));

    // addPlugin("SamplePlugin");
  }

  private void setRightPanel(Plugin panel) {
    rightPanel = panel;
    splitPane.add(panel, JSplitPane.RIGHT);
  }
}
