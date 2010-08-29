package ru.ipo.dces.client;

import ru.ipo.dces.client.resources.Resources;
import ru.ipo.dces.clientservercommunication.ProblemDescription;
import ru.ipo.dces.clientservercommunication.SubmitSolutionRequest;
import ru.ipo.dces.clientservercommunication.SubmitSolutionResponse;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;
import ru.ipo.dces.exceptions.ServerReturnedError;
import ru.ipo.dces.log.LogMessageType;
import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.pluginapi.PluginEnvironment;
import ru.ipo.dces.server.ServerFacade;
import ru.ipo.problemsapi.Problem;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;

public class PluginEnvironmentImpl implements PluginEnvironment {

  private final ProblemDescription pd;  
  private int tabIndex = -1;
  private String cachedTitle = null;

  private static final Icon TAB_ICON = new ImageIcon(Resources.getInstance().getResourceAsByteArray("TabIcon.gif"));
  
  private static HashMap<JPanel, Plugin> panel2plugin = new HashMap<JPanel, Plugin>();

  public PluginEnvironmentImpl(ProblemDescription pd) {
    this.pd = pd;
  }

  public PluginEnvironmentImpl(String pluginName) {
    this.pd = new ProblemDescription();
    this.pd.name = pluginName;
  }

  public void setTitle(String title) {
    if (tabIndex != -1)
      Controller.getClientDialog().getMainTabbedPane().setTitleAt(tabIndex, title);
    else
      cachedTitle = title;
  }

  public HashMap<String, String> submitSolution(HashMap<String, String> solution) throws GeneralRequestFailureException {
    SubmitSolutionRequest ssr = new SubmitSolutionRequest();
    ssr.problemID = pd.id;
    ssr.problemResult = solution;    
    ssr.sessionID = Controller.getContestConnection().getSessionID();
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

    public Problem getProblem() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public JComponent getStatementPanel() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
    return Controller.getContestConnection().getSessionID();
  }

  public int getProblemID() {
    return pd.id;
  }

  public int getTabIndex() {
    return tabIndex;
  }

  public void init(Plugin p) {
    panel2plugin.put(p.getPanel(), p);

    JTabbedPane tabbedPane = Controller.getClientDialog().getMainTabbedPane();
    tabbedPane.addTab("Задача (!!!)", TAB_ICON, p.getPanel());
    tabIndex = tabbedPane.getTabCount() - 1;    
    if (cachedTitle != null)
      setTitle(cachedTitle);
  }

  public static Plugin getPluginByPanel(JPanel panel) {
    return panel2plugin.get(panel);
  }

}
