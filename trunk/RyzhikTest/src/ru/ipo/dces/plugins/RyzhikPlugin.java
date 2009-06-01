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
  private final int BORDER = 10;
  private final int CELL_HEIGHT = 25;
  private final int IMG_WIDTH = 350;
  private final int IMG_HEIGHT = 350;

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
            {{BORDER, IMG_WIDTH, BORDER, 30, BORDER, CELL_WIDTH, 5, CELL_WIDTH, 5, CELL_WIDTH, 5, CELL_WIDTH, 5, CELL_WIDTH, TableLayout.FILL},
                    {BORDER, CELL_HEIGHT, 5, CELL_HEIGHT, 5, CELL_HEIGHT, 5, CELL_HEIGHT, 5, CELL_HEIGHT, 5, CELL_HEIGHT, 5, CELL_HEIGHT, 5, CELL_HEIGHT, 5, CELL_HEIGHT, 5, CELL_HEIGHT, 5, CELL_HEIGHT, TableLayout.FILL}};
    mainPanel.setLayout(new TableLayout(size));
    mainPanel.setBackground(Color.WHITE);
    mainPanel.setBorder(BorderFactory.createMatteBorder(1, 2, 1, 2, Color.BLUE));
    //create label with image
    statement = new JLabel();
    statement.setLayout(new GridLayout(1,1));
    statement.setIcon(new ImageIcon(getStatementGif()));
    statement.setBackground(Color.PINK);
    JPanel butPanel = new JPanel();
    butPanel.setBorder(BorderFactory.createMatteBorder(1, 2, 1, 2, Color.BLUE));
    statement.setBorder(BorderFactory.createMatteBorder(1, 2, 1, 2, Color.BLUE));
    mainPanel.add(statement, "1, 1, 1, 21");
    String comment[] = {"ќцените, пожалуйста, каждое утверждение",
            "'+' - верно всегда",
            "'-' - не верно никогда",
            "'0' - не знаю",
            "'?' - в некоторых случа€х верно, в некоторых - нет",
            "'!' - условие некорректно"
    };

    JLabel commentLabel[] = new JLabel[comment.length];
    for (int i = 0; i < comment.length; i++) {
      commentLabel[i] = new JLabel(comment[i]);
      commentLabel[i].setFont(new Font(commentLabel[i].getFont().getFontName(), commentLabel[i].getFont().getStyle(), 13));
      mainPanel.add(commentLabel[i], "3, " + (2 * i + 1) + ", 14, " + (2 * i + 1));
    }


    //create groups of radiobuttons
    for (int i = 1; i < 6; i++) {

      JLabel item = new JLabel("є" + i);
      item.setFont(new Font(item.getFont().getFontName(), item.getFont().getStyle(), 13));
      mainPanel.add(item, "3, " + (2 * i + 11));
      String symbol[] = {"+", "-", "0", "?", "!"};
      JRadioButton radioButton[] = new JRadioButton[symbol.length];
      ButtonGroup bg = new ButtonGroup();

      for (int j = 0; j < symbol.length; j++) {
        radioButton[j] = new JRadioButton(symbol[j]);
        mainPanel.add(radioButton[j], "" + (2 * j + 5) + ", " + (2 * i + 11));
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
