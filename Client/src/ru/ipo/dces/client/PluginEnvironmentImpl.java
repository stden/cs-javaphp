package ru.ipo.dces.client;

import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.pluginapi.PluginEnvironment;
import ru.ipo.dces.exceptions.ServerReturnedError;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;

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
  public HashMap<String, String> submitSolution(HashMap<String, String> solution) throws GeneralRequestFailureException {
    SubmitSolutionRequest ssr = new SubmitSolutionRequest();
    ssr.problemID = pd.id;
    ssr.problemResult = solution;    
    ssr.sessionID = Controller.getSessionID();
      final SubmitSolutionResponse response;
      try {
          response = Controller.getServer().doRequest(ssr);
      } catch (ServerReturnedError serverReturnedError) {
          //TODO: refactor PHP server to process table exceptions
          throw new GeneralRequestFailureException();
      }
      return response.problemResult;
  }

  public File getProblemFolder() {
    return Controller.getProblemDirectoryByID(pd.id);
  }

  public String getProblemName() {    
    return pd.name;
  }
}
