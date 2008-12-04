package ru.ipo.dces.client;

import javax.swing.JButton;

import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.pluginapi.PluginEnvironment;

import java.util.HashMap;

public class PluginEnvironmentImpl implements PluginEnvironment {

  private JButton                  button;
  private final ProblemDescription pd;

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
  public Response submitSolution(Request solution) throws ServerReturnedError, ServerReturnedNoAnswer {
    SubmitSolutionRequest ssr = new SubmitSolutionRequest();
    ssr.problemID = pd.id;
    ssr.problemResult = new HashMap<String, String>();
    ssr.problemResult.put("asdf", "qwerty");
    ssr.sessionID = Controller.sessionID;
    return Controller.server.doRequest(ssr);
  }
}
