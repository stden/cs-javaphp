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

/**
 * Created by IntelliJ IDEA.
 * User: I. Posov
 * Date: 08.05.2009
 * Time: 15:37:40
 */
@DCESPluginLoadable
public class RyzhikPlugin implements Plugin {

  private JPanel mainPanel;
  private final PluginEnvironment environment;

  private JLabel warningLabel;
  private JButton resubmitSolutionButton;

  public RyzhikPlugin(PluginEnvironment environment) {
    this.environment = environment;

    initInterface();

    //try initialize solution
    try {
      environment.submitSolution(
              SubmissionUtils.setAction("init", new HashMap<String, String>())
      );
    } catch (GeneralRequestFailureException e) {
      //TODO to think what to do if failed to send init request!!!
    }
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

  private void initInterface() {
    environment.setTitle(environment.getProblemName());

    //create panels
    JPanel solutionPanel = new JPanel();
    JPanel warningPanel = new JPanel();
    JPanel selectionPanel = new JPanel();
    mainPanel = new JPanel();

    warningPanel.setVisible(false);

    //set layout for panels
    mainPanel.setLayout(new BorderLayout());
    solutionPanel.setLayout(new BorderLayout());
    warningPanel.setLayout(new BorderLayout());
    selectionPanel.setLayout(new GridLayout(5, 1));

    //add panels to panels
    mainPanel.add(solutionPanel, BorderLayout.CENTER);
    mainPanel.add(warningPanel, BorderLayout.SOUTH);
    solutionPanel.add(selectionPanel, BorderLayout.SOUTH);

    //fill solution panel
    JLabel statement = new JLabel();

    File statementFolder = environment.getProblemFolder();
    String[] gif_files = statementFolder.list(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.endsWith(".gif");
      }
    });

    if (gif_files == null || gif_files.length == 0)
      statement.setText("Не удалось загрузить решение, обратитесь к администраторам");
    else
      statement.setIcon(new ImageIcon(statementFolder.getAbsolutePath() + '/' + gif_files[0]));

    solutionPanel.add(statement, BorderLayout.CENTER);

    //create group of panels with radiobuttons
    for (int i = 1; i < 6; i++) {
      JPanel panel = new JPanel();
      selectionPanel.add(panel);
      JLabel lab = new JLabel("" + i);
      panel.add(lab);
      JRadioButton first = new JRadioButton("?");
      JRadioButton second = new JRadioButton("!");
      JRadioButton third = new JRadioButton("0");
      third.setSelected(true);
      JRadioButton fourth = new JRadioButton("+");
      JRadioButton fifth = new JRadioButton("-");
      panel.add(first);
      panel.add(second);
      panel.add(third);
      panel.add(fourth);
      panel.add(fifth);
      ButtonGroup bg = new ButtonGroup();
      bg.add(first);
      bg.add(second);
      bg.add(third);
      bg.add(fourth);
      bg.add(fifth);

      first.addActionListener(new RadioButtonActionListener(i, first));
      second.addActionListener(new RadioButtonActionListener(i, second));
      third.addActionListener(new RadioButtonActionListener(i, third));
      fourth.addActionListener(new RadioButtonActionListener(i, fourth));
      fifth.addActionListener(new RadioButtonActionListener(i, fifth));
    }

    warningLabel = new JLabel();
    resubmitSolutionButton = new JButton();
    warningPanel.add(warningLabel, BorderLayout.CENTER);
    warningPanel.add(resubmitSolutionButton, BorderLayout.EAST);
  }


  private class RadioButtonActionListener implements ActionListener {

    private int problemNumber;
    private JRadioButton button;

    public RadioButtonActionListener(int problemNumber, JRadioButton button) {
      this.problemNumber = problemNumber;
      this.button = button;
    }

    public void actionPerformed(ActionEvent e) {
      //is called when radio button is pressed

      HashMap<String, String> sol = new HashMap<String, String>();
      sol.put("action", "answer");
      sol.put("question", "" + problemNumber);
      sol.put("answer", button.getText());

      try {
        HashMap<String, String> ans = environment.submitSolution(sol);
      } catch (GeneralRequestFailureException ignored) {
        //do nothing
      }
    }
  }
}
