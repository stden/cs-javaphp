package ru.ipo.dces.client;

import javax.swing.JButton;

import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.pluginapi.PluginEnvironment;

import java.util.HashMap;
import java.io.File;
import java.io.IOException;

public class PluginEnvironmentImpl implements PluginEnvironment {

  private JButton                  button;
  private final ProblemDescription pd;
  public static final String PROBLEMS_DIR = "problems";

  public PluginEnvironmentImpl(ProblemDescription pd) {
    this.pd = pd;
    button = new JButton();
  }

  public JButton getButton() {
    return button;
  }

  public void setButton(JButton button) {
    this.button = button;
  }

  @Override
  public void setTitle(String title) {
    button.setText(title);
  }

  @Override
  public HashMap<String, String> submitSolution(HashMap<String, String> solution) throws ServerReturnedError, ServerReturnedNoAnswer {
    SubmitSolutionRequest ssr = new SubmitSolutionRequest();
    ssr.problemID = pd.id;
    ssr.problemResult = solution;    
    ssr.sessionID = Controller.sessionID;
    ssr.contestID = -1;
    final SubmitSolutionResponse response = Controller.server.doRequest(ssr);
    return response.problemResult;
  }

  public File getProblemFolder() {
    return Controller.getProblemFolder(pd.id);
  }

  public String getProblemName() {    
    return pd.name;
  }
}
