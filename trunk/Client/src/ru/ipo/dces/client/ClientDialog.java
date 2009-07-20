package ru.ipo.dces.client;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.plugins.admin.LoginPluginV2;
import ru.ipo.dces.plugins.admin.ResultsPlugin;
import info.clearthought.layout.TableLayout;

public class ClientDialog extends JFrame {

  private JTextPane logTextPane;
  private JTabbedPane mainTabbedPane;
  private Plugin selectedPlugin = null;  

  public JTabbedPane getMainTabbedPane() {
    return mainTabbedPane;
  }

  /**
   * Удалить все Plugin'ы
   */
  public void clearLeftPanel() {
    mainTabbedPane.removeAll();
  }

  public void initGUI() {
    //set outer components and outer layout
    TableLayout outerLayout = new TableLayout(new double[][]
            {{TableLayout.FILL}, {20, TableLayout.FILL, 60}}
    );    getContentPane().setLayout(outerLayout);
    logTextPane = new JTextPane();
    mainTabbedPane = new JTabbedPane(JTabbedPane.LEFT, JTabbedPane.SCROLL_TAB_LAYOUT);
    JScrollPane logScrollPane = new JScrollPane(logTextPane);
    getContentPane().add(Controller.getContestChooserPanel(), "0, 0");
    getContentPane().add(mainTabbedPane, "0, 1");
    getContentPane().add(logScrollPane, "0, 2");

    //logTextPane adjust
    logTextPane.setEditable(false);
    logTextPane.setBackground(Color.BLACK);

    mainTabbedPane.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        int selectedIndex = mainTabbedPane.getSelectedIndex();
        Plugin newPlugin = null;
        if (selectedIndex != -1)
          newPlugin = PluginEnvironmentImpl.getPluginByPanel (
                  (JPanel) mainTabbedPane.getComponentAt(selectedIndex)
          );

        if (selectedPlugin != null)
          selectedPlugin.deactivate();
        if (newPlugin != null) {
          //TODO remove the next 'if'
          if (newPlugin instanceof AdminPlugin && Controller.getContestDescription() != null)
            ((AdminPlugin)newPlugin).contestSelected(Controller.getContestDescription());
          newPlugin.activate();
        }

        selectedPlugin = newPlugin;
      }
    });

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
    pe1.init(new LoginPluginV2(pe1));

    PluginEnvironmentImpl pe2 = new PluginEnvironmentImpl(Localization.getAdminPluginName(ResultsPlugin.class));
    pe2.init(new ResultsPlugin(pe2));
  }

  public JTextPane getLogTextPane() {
    return logTextPane;
  }

}
