package ru.ipo.dces.client;

import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.pluginapi.PluginEnvironment;

import java.util.HashMap;
import java.io.File;

public class PluginEnvironmentImpl implements PluginEnvironment {

  private PluginEnvironmentView view;
  private final ProblemDescription pd;
  public static final String PROBLEMS_DIR = "problems";

  public PluginEnvironmentImpl(ProblemDescription pd) {
    this.pd = pd;
    view = new PluginEnvironmentView();
  }

  public PluginEnvironmentView getView() {
    return view;
  }

  @Override
  public void setTitle(String title) {
    view.setTitle(title);
  }

  @Override
  public HashMap<String, String> submitSolution(HashMap<String, String> solution) throws ServerReturnedError, ServerReturnedNoAnswer {
    SubmitSolutionRequest ssr = new SubmitSolutionRequest();
    ssr.problemID = pd.id;
    ssr.problemResult = solution;    
    ssr.sessionID = Controller.getSessionID();
    ssr.contestID = -1;
    final SubmitSolutionResponse response = Controller.getServer().doRequest(ssr);
    return response.problemResult;
  }

  public File getProblemFolder() {
    return Controller.getProblemDirectoryByID(pd.id);
  }

  public String getProblemName() {    
    return pd.name;
  }
}
