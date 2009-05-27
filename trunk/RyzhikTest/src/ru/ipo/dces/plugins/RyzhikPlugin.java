package ru.ipo.dces.plugins;

import ru.ipo.dces.pluginapi.PluginEnvironment;
import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.pluginapi.DCESPluginLoadable;
import ru.ipo.dces.utils.SubmissionUtils;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;

import javax.swing.*;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.io.FilenameFilter;
import java.io.File;

import info.clearthought.layout.TableLayout;

/**
 * Created by IntelliJ IDEA.
 * User: I. Posov
 * Date: 08.05.2009
 * Time: 15:37:40
 */
@DCESPluginLoadable
public class RyzhikPlugin implements Plugin {

  private JPanel mainPanel;
  private PluginEnvironment myEnvironment;
  private JLabel statement;

  private final int CELL_WIDTH = 33;
  private final int BORDER = 20;
  private final int CELL_HEIGHT = 150;
  private final int IMG_WIDTH = 350;

  public RyzhikPlugin(PluginEnvironment pe) {
    myEnvironment = pe;
    myEnvironment.setTitle(pe.getProblemName());
    mainPanel = new JPanel();

    //send 'init'
    try {
      myEnvironment.submitSolution(
              SubmissionUtils.setAction("init", new HashMap<String, String>())
      );
    } catch (GeneralRequestFailureException e) {
      //DO NOTHING
    }

    //create size for tablelayout
    double size[][] =
            {{BORDER, IMG_WIDTH, BORDER, 40, 10, CELL_WIDTH, 5, CELL_WIDTH, 5, CELL_WIDTH, 5, CELL_WIDTH, 5, CELL_WIDTH, TableLayout.FILL},
                    {BORDER, CELL_HEIGHT, 16, 10, 16, 10, 16, 10, 16, 10, 16, CELL_HEIGHT, TableLayout.FILL}};
    mainPanel.setLayout(new TableLayout(size));
    //create label with image
    statement = new JLabel();
    statement.setIcon(new ImageIcon(getStatementGif()));
    mainPanel.add(statement, "1, 1, 1, 11");
    //create groups of radiobuttons
    for (int i = 1; i < 6; i++) {

      JLabel item = new JLabel("Пункт " + i);
      mainPanel.add(item, "3, " + (2 * i));
      String symbol[] = {"+", "-", "0", "?", "!"};
      JRadioButton radioButton[] = new JRadioButton[symbol.length];
      ButtonGroup bg = new ButtonGroup();

      for (int j = 0; j < symbol.length; j++) {
        radioButton[j] = new JRadioButton(symbol[j]);
        mainPanel.add(radioButton[j], "" + (2 * j + 5) + ", " + (2 * i));
        bg.add(radioButton[j]);
        radioButton[j].addActionListener(new RadioButtonActionListener(i, radioButton[j], myEnvironment));
        if (symbol[j].equals("0"))
          radioButton[j].setSelected(true);
      }
    }
  }

  private String getStatementGif() {
    File folder = myEnvironment.getProblemFolder();
    String[] gifs = folder.list(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.endsWith(".gif");
      }
    });
    //TODO assert there is exactly one gif
    return folder.getAbsolutePath() + "/" + gifs[0];
  }

  public JPanel getPanel() {
    return mainPanel;
  }

  public void activate() {
    //do nothing
  }

  public void deactivate() {
    //do nothing
  }


}
