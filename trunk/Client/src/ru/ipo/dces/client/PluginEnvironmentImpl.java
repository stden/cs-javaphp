package ru.ipo.dces.client;

import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.pluginapi.PluginEnvironment;
import ru.ipo.dces.exceptions.ServerReturnedError;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;
import ru.ipo.dces.log.LogMessageType;
import ru.ipo.dces.server.ServerFacade;

import java.util.HashMap;
import java.io.File;

public class PluginEnvironmentImpl implements PluginEnvironment {

  private final PluginEnvironmentView view = new PluginEnvironmentView();
  private final ProblemDescription pd;
  public static final String PROBLEMS_DIR = "problems";

  public PluginEnvironmentImpl(ProblemDescription pd) {
    this.pd = pd;
  }

  public PluginEnvironmentImpl(String pluginName) {
    this.pd = new ProblemDescription();
    this.pd.name = pluginName;
  }

  public PluginEnvironmentView getView() {
    return view;
  }

  public void setTitle(String title) {
    view.setTitle(title);
  }

  public HashMap<String, String> submitSolution(HashMap<String, String> solution) throws GeneralRequestFailureException {
    SubmitSolutionRequest ssr = new SubmitSolutionRequest();
    ssr.problemID = pd.id;
    ssr.problemResult = solution;    
    ssr.sessionID = Controller.getSessionID();
      final SubmitSolutionResponse response;
      try {
          response = Controller.getServer().doRequest(ssr);
      } catch (ServerReturnedError err) {
          Controller.getLogger().log(
                  err.getMessage(),
                  LogMessageType.Error,
                  Localization.LOGGER_NAME
          );
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

  public void log(String message, LogMessageType type) {
    Controller.getLogger().log(
            message,
            type,
            getProblemName()
    );
  }

  public ServerFacade getServer() {
    return Controller.getServer();
  }

  public String getSessionID() {
    return Controller.getSessionID();
  }

  public int getProblemID() {
    return pd.id;
  }
}
