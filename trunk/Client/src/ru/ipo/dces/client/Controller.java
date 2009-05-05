package ru.ipo.dces.client;

import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.debug.PluginBox;
import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.pluginapi.PluginEnvironment;
import ru.ipo.dces.plugins.admin.*;
import ru.ipo.dces.exceptions.ServerReturnedError;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;
import ru.ipo.dces.server.ServerFacade;
import ru.ipo.dces.server.http.HttpServer;
import ru.ipo.dces.log.ConsoleUserMessagesLogger;
import ru.ipo.dces.log.LogMessageType;
import ru.ipo.dces.log.UserMessagesLogger;
import ru.ipo.dces.utils.FileSystemUtils;
import ru.ipo.dces.utils.ZipUtils;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Date;
import java.text.DateFormat;

/**
 * Контроллер, который хранит данные о соединении с сервером и позволяет
 * выполнять стандартные действия с сервером
 */
public class Controller {
  private static ServerFacade server;
  private static String             sessionID;
  private static ClientDialog       clientDialog;
  private static UserDescription.UserType userType;
  private static UserMessagesLogger logger;
  
  private static ContestChoosingPanel contestChooserPanel;
  private static Plugin currentPlugin;

  private static LogoutPlugin logoutPlugin;

  public static void log(ServerReturnedError err) {
    getLogger().log(err.getMessage(), LogMessageType.Error, Localization.LOGGER_NAME);
    System.out.print("ERROR:");
    System.out.println(err.getMessage());
    if (err.getExtendedInfo() != null) {
      System.out.print("extended info:");
      System.out.println(err.getExtendedInfo());
    }
  }

  /** Добавление Plugin'а в клиент
   * @param pd the description of the problem for which the plugin is added
   */
  public static void addPlugin(ProblemDescription pd) {
    PluginEnvironmentImpl pe = new PluginEnvironmentImpl(pd);

    Plugin p;
    try {
        Class<? extends Plugin> mainClass = PluginLoader.getPluginClass(pd.clientPluginAlias);
        //NullPointerException is possible in the next line
        Constructor<? extends Plugin> constructor = mainClass.getConstructor(PluginEnvironment.class);
        p = constructor.newInstance((PluginEnvironment) pe);
    } catch (Exception e) {
        //something wrong with plugins
      p = null;
    }

    clientDialog.addPluginToForm(pe.getView(), p);
  }

  /** Добавление Plugin'а в клиент
   * @param pluginClass class of new Plugin
   * @return added plugin
   */
  public static AdminPlugin addAdminPlugin(Class<? extends AdminPlugin> pluginClass) {
    try {
      PluginEnvironmentImpl pe = new PluginEnvironmentImpl(Localization.getAdminPluginName(pluginClass));
      final Constructor<? extends AdminPlugin> constructor = pluginClass.getConstructor(PluginEnvironment.class);
      AdminPlugin p = constructor.newInstance(pe);
      clientDialog.addPluginToForm(pe.getView(), p);
      return p;
    } catch (Exception e) {
      System.out.println("Admin plugin " + pluginClass.getSimpleName() + " has no constuctor(PluginEnvironment)");
      System.exit(1); //it should not occur
      return null;
    }
  }

  public static void login(String login, char[] password, ContestDescription contest) {
    try {
      ConnectToContestRequest request = new ConnectToContestRequest();
      request.contestID = contest.contestID;

      contestChooserPanel.setContest(contest);

      request.login = login;
      // TODO improve the security here
      request.password = new String(password);
      ConnectToContestResponse res = Controller.server.doRequest(request);
      Controller.sessionID = res.sessionID;

      //show message - 'ok, connected, finish is at...'
      String leftTimeMessage;
      switch (res.user.userType) {
        case SuperAdmin:
          leftTimeMessage = "Суперадминистратор";
          break;
        case ContestAdmin:
          leftTimeMessage = "Администратор соревнования";
          break;
        case Participant:
          Date now = new Date();
          if (now.before(res.finishTime))
            //TODO format only time if the finish is today
            leftTimeMessage = "Окончание соревнования в " + DateFormat.getInstance().format(res.finishTime);
          else
            leftTimeMessage = "Соревнование уже закончилось";
          break;
        default:
          leftTimeMessage = "?? Неизвестный тип пользователя";
      }

      getLogger().log(
              "Успешное подключение к соревнованию. " + leftTimeMessage,
              LogMessageType.OK,
              Localization.LOGGER_NAME
      );

      // Удаляем все запущенные Plugin'ы
      clientDialog.clearLeftPanel();

      // Если пользователь администратор или администратор сервера
      // загружаем ему административные Plugin'ы
      userType = res.user.userType;
      switch (res.user.userType) {
        case ContestAdmin:
          contestChooserPanel.setChooserVisible(false);
          addAdminPlugin(AdjustContestsPlugin.class);
          addAdminPlugin(ManageUsersPlugin.class);
          addAdminPlugin(ResultsPlugin.class);
          setLogoutPlugin((LogoutPlugin) addAdminPlugin(LogoutPlugin.class));
          break;
        case SuperAdmin:
          contestChooserPanel.setChooserVisible(true);
          addAdminPlugin(CreateContestPlugin.class);
          addAdminPlugin(AdjustContestsPlugin.class);
          addAdminPlugin(ManageUsersPlugin.class);
          addAdminPlugin(PluginsManagementPlugin.class);
          addAdminPlugin(ResultsPlugin.class);
          setLogoutPlugin((LogoutPlugin) addAdminPlugin(LogoutPlugin.class));
          break;
        case Participant:
          contestChooserPanel.setChooserVisible(false);
          // Получаем данные о задачах
          refreshParticipantInfo(false, false);
      }

    } catch (ServerReturnedError e) {
      Controller.log(e);
      clientDialog.initialState();
    } catch (GeneralRequestFailureException e) {
      //log nothing
      clientDialog.initialState();
    } catch (IOException e) {
      Controller.getLogger().log("Не удалось загрузить плагины", LogMessageType.Error, Localization.LOGGER_NAME);
      clientDialog.initialState();
    }
  }

  private static void setLogoutPlugin(LogoutPlugin logoutPlugin) {
    Controller.logoutPlugin = logoutPlugin;
    if (logoutPlugin != null)
      logoutPlugin.setStopContestControlsVisible(getContestDescription().contestTiming.selfContestStart);
  }

  public static void refreshParticipantInfo(boolean refreshProblems, boolean refreshPlugins) throws ServerReturnedError, GeneralRequestFailureException, IOException {
    GetContestDataRequest rq = new GetContestDataRequest();
    rq.contestID = -1;
    rq.infoType = GetContestDataRequest.InformationType.ParticipantInfo;
    rq.extendedData = null; //TODO download only problems that are not already on disk
    rq.sessionID = sessionID;
    GetContestDataResponse rs = Controller.server.doRequest(rq);

    if (refreshPlugins) {
      //get contest plugins
      HashSet<String> contestPlugins = new HashSet<String>();
      for (ProblemDescription pd : rs.problems)
        contestPlugins.add(pd.clientPluginAlias);
      //remove contest plugins
      for (String contestPlugin : contestPlugins) {
          File pluginFile = new File(Settings.getInstance().getPluginsDirectory() + '/' + contestPlugin + ".jar");
          if (!pluginFile.delete())
              getLogger().log(
                      "Не удалось обновить плагин: " + pluginFile.getName(),
                      LogMessageType.Error,
                      Localization.LOGGER_NAME
              );
      }
      PluginLoader.clear();
    }

    //fill dirs with data
    for (ProblemDescription pd : rs.problems) {
      File problemFolder = getProblemDirectoryByID(pd.id);

      if (problemFolder.exists()) {
        FileSystemUtils.deleteDir(problemFolder);
        problemFolder.mkdir();
      }
      else
        problemFolder.mkdirs();

      ZipUtils.unzip(pd.statement, problemFolder);

      addPlugin(pd);
    }

    addAdminPlugin(ResultsPlugin.class);
    setLogoutPlugin((LogoutPlugin) addAdminPlugin(LogoutPlugin.class));
  }

  /** Завершение сессии пользователя */
  public static void logout() {
    DisconnectRequest dr = new DisconnectRequest();
    dr.sessionID = sessionID;

    try {
        server.doRequest(dr);
        contestChooserPanel.setContest(null);
    } catch (ServerReturnedError sre) {
        log(sre);
    } catch (GeneralRequestFailureException grfe) {
      //do nothing
    }

    Controller.sessionID = null;
    Controller.userType = null;
    clientDialog.initialState();
    contestChooserPanel.setChooserVisible(true);
  }

    public static UserMessagesLogger getLogger() {
      if (logger == null) {
        if (clientDialog != null)
          logger = new TextPaneUserMessagesLogger(clientDialog.getLogTextPane());
        else
          logger = new ConsoleUserMessagesLogger();
      }
      return logger;      
    }

    /**
   * Запуск клиента
   *
   * @param args the command line input
   */
  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      System.out.println("Failed to set system look and feel");
      System.exit(1);
    }

    server = new HttpServer(Settings.getInstance().getHost());

    processArgs(args);

    clientDialog = new ClientDialog();
    clientDialog.initGUI();
    clientDialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    clientDialog.setVisible(true);
  }

  private static void processArgs(String[] args) {
    if (args.length == 0) return;

    if (args[0].equals("-help")) {
      System.out.println("Usage: -help shows this help");
      System.out.println("       -createdb <superuser-login> <superuser-password>");
      System.exit(0);
    }
    else if (args[0].equals("-createdb")) {
      if (args.length != 3)
        System.out.println("need exactly two additional paramenters: superuser login and superuser password"); 
      else
      {
        final CreateDataBaseRequest createDataBaseRequest = new CreateDataBaseRequest();
        createDataBaseRequest.login = args[1];
        createDataBaseRequest.password = args[2];
        try {
          server.doRequest(createDataBaseRequest);
        } catch (ServerReturnedError sre) {
          System.out.println("Server returned error: " + sre.getMessage());
          System.exit(1);
        } catch (GeneralRequestFailureException srna) {
          System.out.println("Server returned no answer: " + srna.getMessage());
          System.exit(1);
        }

        System.out.println("Database successfully created");
        System.exit(0);
      }
    }
  }

  public static ContestDescription[] getAvailableContests() {
    try {
      AvailableContestsResponse res = Controller.server
          .doRequest(new AvailableContestsRequest());
      return res.contests;
    } catch (GeneralRequestFailureException e) {
      //log nothing
    } catch (ServerReturnedError serverReturnedError) {
      Controller.log(serverReturnedError);
    }

    return new ContestDescription[]{};
  }

  public static boolean addContest(ContestDescription cd) {
      try {
          CreateContestRequest contestRequest = new CreateContestRequest();
          contestRequest.sessionID = sessionID;
          contestRequest.contest = cd;
          server.doRequest(contestRequest);
      } catch (ServerReturnedError e) {
          Controller.log(e);
          return false;
      } catch (GeneralRequestFailureException e) {
          //log nothing
          return false;
      }

      return true;
  }

    public static GetContestDataResponse getContestData(int contestID) {
      GetContestDataRequest gcdr = new GetContestDataRequest();
      gcdr.contestID = contestID;
      gcdr.extendedData = null;
      gcdr.infoType = GetContestDataRequest.InformationType.NoInfo;
      gcdr.sessionID = sessionID;

      try {
        return server.doRequest(gcdr);
      } catch (ServerReturnedError serverReturnedError) {
        Controller.log(serverReturnedError);
        return null;
      } catch (GeneralRequestFailureException GeneralRequestFailureException) {
        //log nothing
        return null;
      }
    }

    public static boolean adjustContestData(AdjustContestRequest acr) {
        try {
          acr.sessionID = sessionID;
          server.doRequest(acr);
        } catch (ServerReturnedError serverReturnedError) {
            log(serverReturnedError);
        } catch (GeneralRequestFailureException GeneralRequestFailureException) {
            return false;
        }
        return true;
    }

  public static void registerAnonymouslyToContest(String login, char[] password, int contestID, String[] userData) {
    RegisterToContestRequest r = new RegisterToContestRequest();
    r.sessionID = null;
    r.contestID = contestID;
    r.user = new UserDescription();
    r.user.dataValue = userData;
    r.user.login = login;
    r.user.password = new String(password);
    r.user.userType = UserDescription.UserType.Participant;

    try {
      server.doRequest(r);      
      Controller.getLogger().log("Регистрация прошла успешно", LogMessageType.OK, Localization.LOGGER_NAME);
    } catch (ServerReturnedError serverReturnedError) {
      Controller.log(serverReturnedError);
    } catch (GeneralRequestFailureException GeneralRequestFailureException) {
      //log nothing
    }
  }

  public static File getProblemDirectoryByID(int problemID) {
    return new File(Settings.getInstance().getProblemsDirectory() + '/' + problemID);
  }

  public static File getProblemDebugDirectoryByID(int problemID) {
    final File debugDir = new File(Settings.getInstance().getProblemsDirectory() + "/debug");
    debugDir.mkdir();
    return new File(debugDir.getPath() + '/' + problemID);
  }

  public static String getSessionID() {
    return sessionID;
  }

  public static ServerFacade getServer() {
    return server;
  }

  public static int getContestID() {
    return contestChooserPanel.getContest().contestID;
  }

  /**
   * Returns user type of the user currently logged in or null if client is not connected to a contest
   * @return user type of the user currently logged  in or null if client is not connected to a contest
   */
  public static UserDescription.UserType getUserType() {
    return userType;
  }

  public static void debugProblem(int problemID, int contestID) {
    //create and fill get contest data request
    final GetContestDataRequest contestRequest = new GetContestDataRequest();
    contestRequest.contestID = contestID;
    contestRequest.extendedData = new int[] {problemID};
    contestRequest.infoType = GetContestDataRequest.InformationType.ParticipantInfo;
    contestRequest.sessionID = sessionID;

    //do a request and get a response
    final GetContestDataResponse contestResponse;
    try {
      contestResponse = Controller.getServer().doRequest(contestRequest);
    } catch (ServerReturnedError e) {
      System.out.println("DEBUG error:");
      e.printStackTrace();
      return;
    } catch (GeneralRequestFailureException e) {
      System.out.println("DEBUG error:");
      e.printStackTrace();
      return;
    }

    //find problem with the desired id
    ProblemDescription problem = null;
    for (ProblemDescription p : contestResponse.problems)
      if (p.id == problemID)
        problem = p;

    if (problem == null) {
      System.out.println("Debug error: can not debug problem id " + problemID + ". It is not found on the server");
      return;
    }

    //run debug
    final Class<? extends Plugin> pluginClass = PluginLoader.getPluginClass(problem.clientPluginAlias);
    PluginBox box = new PluginBox(pluginClass, new ServerPluginProxy(problem), problem.name);
    box.setVisible(true);    
  }

  public static ClientDialog getClientDialog() {
    return clientDialog;
  }

    public static UserDescription[] getUsers(int contestID) {
        final GetUsersRequest req = new GetUsersRequest();

        req.contestID = contestID;
        req.sessionID = sessionID;

        try {
            GetUsersResponse res = server.doRequest(req);

            return res.users;

        } catch (ServerReturnedError e) {
            Controller.log(e);
        } catch (GeneralRequestFailureException e) {
            //log nothing
        }

        return new UserDescription[]{};
    }

    public static ContestDescription getContestDescription() {
        return contestChooserPanel.getContest();
    }

  public static void adjustClientPlugin(String alias, String description, File file) throws ServerReturnedError, GeneralRequestFailureException {
    AdjustClientPluginRequest r = new AdjustClientPluginRequest();

    r.pluginAlias = alias;

    if (description.equals(""))
      r.description = null;
    else
      r.description = description;

    r.sessionID = sessionID;

    //load plugin data from file
    if (file != null) {
      r.pluginData = new byte[(int)file.length()];
      try {
        InputStream is = new FileInputStream(file);
        if (is.read(r.pluginData) < r.pluginData.length) throw new IOException();
      } catch (IOException e) {
        throw new ServerReturnedError(0, "");
      }
    }
    else
      r.pluginData = null;

    server.doRequest(r);
  }

  public static void removeClientPlugin(String alias) throws ServerReturnedError, GeneralRequestFailureException {
    RemoveClientPluginRequest r = new RemoveClientPluginRequest();
    r.pluginAlias = alias;
    r.sessionID = sessionID;
    server.doRequest(r);
  }

    public static void addUser(String login, char[] password, String[] dataValue, UserDescription.UserType ut, int contestID) throws ServerReturnedError, GeneralRequestFailureException {

        RegisterToContestRequest cur = new RegisterToContestRequest();

        UserDescription ud = new UserDescription();

        ud.login = login;
        ud.password = new String(password);
        ud.dataValue = dataValue;
        ud.userType = ut;

        cur.contestID = contestID;
        cur.sessionID = sessionID;
        cur.user = ud;

        server.doRequest(cur);
    }

    public static void removeUser(int userID) throws ServerReturnedError, GeneralRequestFailureException {
        RemoveUserRequest rur = new RemoveUserRequest();

        rur.sessionID = sessionID;
        rur.userID = userID;

        server.doRequest(rur);
    }

  public static void setFreeze(boolean freeze) {
    if (clientDialog == null) return;

    clientDialog.setEnabled(!freeze);
  }

  public static ContestChoosingPanel getContestChooserPanel() {
    return contestChooserPanel == null ? contestChooserPanel = new ContestChoosingPanel() : contestChooserPanel;
  }

  public static void contestSelected(ContestDescription contest) {    
    if (currentPlugin instanceof AdminPlugin) {
      AdminPlugin plugin = (AdminPlugin)currentPlugin;
      plugin.contestSelected(contest);
    }
  }

  public static void setCurrentPlugin(Plugin currentPlugin) {
    Controller.currentPlugin = currentPlugin;

    if (currentPlugin instanceof AdminPlugin) {
      AdminPlugin plugin = (AdminPlugin)currentPlugin;
      plugin.contestSelected(getContestDescription());
    }
  }

  public static Plugin getCurrentPlugin() {
    return currentPlugin;
  }

  public static void stopContest() {
    StopContestRequest r = new StopContestRequest();
    try {
      server.doRequest(r);
      getLogger().log("Соревнование остановлено", LogMessageType.OK, Localization.LOGGER_NAME);
    } catch (ServerReturnedError e) {
      log(e);
    } catch (GeneralRequestFailureException e) {
      //do nothing - everything already logged
    }
  }
}
