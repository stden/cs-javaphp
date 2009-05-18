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

  private final int CELL_WIDTH = 36;

  public RyzhikPlugin(PluginEnvironment pe) {
    myEnvironment = pe;
    myEnvironment.setTitle(/*pe.getProblemName()*/"Задача");
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
            {{165, 50, 10, CELL_WIDTH, 10, CELL_WIDTH, 10, CELL_WIDTH, 10, CELL_WIDTH, 10, CELL_WIDTH, TableLayout.FILL},
                    {25, 314, 20, 16, 10, 16, 10, 16, 10, 16, 10, 16, TableLayout.FILL}};
    mainPanel.setLayout(new TableLayout(size));
    //create label with image
    statement = new JLabel();
    statement.setIcon(new ImageIcon(getStatementGif()));
    mainPanel.add(statement, "1, 1, 12, 1");
    //create groups of radiobuttons
    for (int i = 1; i < 6; i++) {

      JLabel item = new JLabel("Пункт " + i);
      mainPanel.add(item, "1, " + (2 * i + 1));
      String symbol[] = {"+", "-", "0", "?", "!"};
      JRadioButton radioButton[] = new JRadioButton[symbol.length];
      ButtonGroup bg = new ButtonGroup();

      for (int j = 0; j < symbol.length; j++) {
        radioButton[j] = new JRadioButton(symbol[j]);
        mainPanel.add(radioButton[j], "" + (2 * j + 3) + ", " + (2 * i + 1));
        bg.add(radioButton[j]);
        radioButton[j].addActionListener(new RadioButtonActionListener(i, radioButton[j], myEnvironment));
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

//  private void initInterface() {
//    environment.setTitle(environment.getProblemName());
//
//    //create panels
//    JPanel solutionPanel = new JPanel();
//    JPanel warningPanel = new JPanel();
//    JPanel selectionPanel = new JPanel();
//    mainPanel = new JPanel();
//
//    warningPanel.setVisible(false);
//
//    //set layout for panels
//    mainPanel.setLayout(new BorderLayout());
//    solutionPanel.setLayout(new BorderLayout());
//    warningPanel.setLayout(new BorderLayout());
//    selectionPanel.setLayout(new GridLayout(5, 1));
//
//    //add panels to panels
//    mainPanel.add(solutionPanel, BorderLayout.CENTER);
//    mainPanel.add(warningPanel, BorderLayout.SOUTH);
//    solutionPanel.add(selectionPanel, BorderLayout.SOUTH);
//
//    //fill solution panel
//    JLabel statement = new JLabel();
//
//    File statementFolder = environment.getProblemFolder();
//    String[] gif_files = statementFolder.list(new FilenameFilter() {
//      public boolean accept(File dir, String name) {
//        return name.endsWith(".gif");
//      }
//    });
//
//    if (gif_files == null || gif_files.length == 0)
//      statement.setText("Не удалось загрузить решение, обратитесь к администраторам");
//    else
//      statement.setIcon(new ImageIcon(statementFolder.getAbsolutePath() + '/' + gif_files[0]));
//
//    solutionPanel.add(statement, BorderLayout.CENTER);
//
//    //create group of panels with radiobuttons
//    for (int i = 1; i < 6; i++) {
//      JPanel panel = new JPanel();
//      selectionPanel.add(panel);
//      JLabel lab = new JLabel("" + i);
//      panel.add(lab);
//      JRadioButton first = new JRadioButton("?");
//      JRadioButton second = new JRadioButton("!");
//      JRadioButton third = new JRadioButton("0");
//      third.setSelected(true);
//      JRadioButton fourth = new JRadioButton("+");
//      JRadioButton fifth = new JRadioButton("-");
//      panel.add(first);
//      panel.add(second);
//      panel.add(third);
//      panel.add(fourth);
//      panel.add(fifth);
//      ButtonGroup bg = new ButtonGroup();
//      bg.add(first);
//      bg.add(second);
//      bg.add(third);
//      bg.add(fourth);
//      bg.add(fifth);
//
//      first.addActionListener(new RadioButtonActionListener(i, first));
//      second.addActionListener(new RadioButtonActionListener(i, second));
//      third.addActionListener(new RadioButtonActionListener(i, third));
//      fourth.addActionListener(new RadioButtonActionListener(i, fourth));
//      fifth.addActionListener(new RadioButtonActionListener(i, fifth));
//    }
//
//    warningLabel = new JLabel();
//    resubmitSolutionButton = new JButton();
//    warningPanel.add(warningLabel, BorderLayout.CENTER);
//    warningPanel.add(resubmitSolutionButton, BorderLayout.EAST);
//  }


//  private class RadioButtonActionListener implements ActionListener {
//
//    private int problemNumber;
//    private JRadioButton button;
//
//    public RadioButtonActionListener(int problemNumber, JRadioButton button) {
//      this.problemNumber = problemNumber;
//      this.button = button;
//    }
//
//    public void actionPerformed(ActionEvent e) {
//      //is called when radio button is pressed
//
//      HashMap<String, String> sol = new HashMap<String, String>();
//      sol.put("action", "answer");
//      sol.put("question", "" + problemNumber);
//      sol.put("answer", button.getText());
//
//      try {
//        HashMap<String, String> ans = environment.submitSolution(sol);
//      } catch (GeneralRequestFailureException ignored) {
//        //do nothing
//      }
//    }
//  }
}
