package ru.ipo.dces.client;

import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.debug.PluginBox;
import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.pluginapi.PluginEnvironment;
import ru.ipo.dces.plugins.admin.*;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.Constructor;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.HashSet;

/**
 * Контроллер, который хранит данные о соединении с сервером и позволяет
 * выполнять стандартные действия с сервером
 */
public class Controller {
  private static ServerFacade       server;
  private static String             sessionID;
  private static ContestDescription contestDescription;
  private static ClientDialog       clientDialog;
  private static UserDescription.UserType userType;

  /** Добавление Plugin'а в клиент
   * @param pd the description of the problem for which the plugin is added
   */
  public static void addPlugin(ProblemDescription pd) {
    PluginEnvironmentImpl pe = new PluginEnvironmentImpl(pd);

    Plugin p = PluginLoader.load(pd.clientPluginAlias, pe);

    clientDialog.addPluginToForm(pe.getView(), p);
  }

  /** Добавление Plugin'а в клиент
   * @param pluginClass class of new Plugin
   */
  public static void addAdminPlugin(Class<? extends Plugin> pluginClass) {
    try {
      PluginEnvironmentImpl pe = new PluginEnvironmentImpl(null);
      final Constructor<? extends Plugin> constructor = pluginClass.getConstructor(PluginEnvironment.class);
      Plugin p = constructor.newInstance(pe);
      clientDialog.addPluginToForm(pe.getView(), p);
    } catch (Exception e) {
      System.exit(1); //it should not occur
    }
  }

  public static void login(String login, char[] password, ContestDescription contest) {
    try {
      ConnectToContestRequest request = new ConnectToContestRequest();
      request.contestID = contest.contestID;
      Controller.contestDescription = contest;
      request.login = login;
      // TODO improve the security here
      request.password = new String(password);
      ConnectToContestResponse res = Controller.server.doRequest(request);
      Controller.sessionID = res.sessionID;
      //TODO save contest description in 'contestDescription'

      // Удаляем все запущенные Plugin'ы
      clientDialog.clearLeftPanel();

      // Если пользователь администратор или администратор сервера
      // загружаем ему административные Plugin'ы
      userType = res.user.userType;
      switch (res.user.userType) {
        case ContestAdmin:
          addAdminPlugin(AdjustContestsPlugin.class);
          addAdminPlugin(ManageUsersPlugin.class);
          addAdminPlugin(LogoutPlugin.class);
          break;
        case SuperAdmin:
          addAdminPlugin(CreateContestPlugin.class);
          addAdminPlugin(AdjustContestsPlugin.class);
          addAdminPlugin(ManageUsersPlugin.class);          
          addAdminPlugin(PluginsManagementPlugin.class);          
          addAdminPlugin(LogoutPlugin.class);
          break;
        case Participant:
          // Получаем данные о задачах
          refreshParticipantInfo(false, false);
      }
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, "При попытке подключения к контесту произошла ошибка: " + e.getMessage(), "Ошибка",
          JOptionPane.ERROR_MESSAGE);
      clientDialog.initialState();
    }
  }

  public static void refreshParticipantInfo(boolean refreshProblems, boolean refreshPlugins) throws ServerReturnedError, ServerReturnedNoAnswer, IOException {
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
      for (String contestPlugin : contestPlugins)
        new File(Settings.getInstance().getPluginsDirectory() + '/' + contestPlugin).delete();  
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

      unzip(pd.statement, problemFolder);

      addPlugin(pd);
    }

    addAdminPlugin(LogoutPlugin.class);
  }

  //TODO test this method to work with directories inside an archive
  public static void unzip(byte[] zip, File folder) throws IOException {
    int BUFFER = 4096;
    BufferedOutputStream dest;
    ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zip));
    ZipEntry entry;
    while ((entry = zis.getNextEntry()) != null) {
      int count;
      byte data[] = new byte[BUFFER];
      if (entry.isDirectory()) {
        (new File(entry.getName())).mkdirs();
        continue;
      }

      // write the files to the disk
      File fout = new File(folder.getCanonicalPath() + '/' + entry.getName());
      FileSystemUtils.ensureFileHasPath(fout);
      FileOutputStream fos = new FileOutputStream(fout);
      dest = new BufferedOutputStream(fos, BUFFER);
      while ((count = zis.read(data, 0, BUFFER)) != -1) {
        dest.write(data, 0, count);
      }
      dest.flush();
      dest.close();
    }
    zis.close();
  }

  /** Завершение сессии пользователя */
  public static void logout() {
    DisconnectRequest dr = new DisconnectRequest();
    dr.sessionID = sessionID;

    try {
        server.doRequest(dr);
        Controller.contestDescription = null;
    } catch (Exception serverReturnedError) {
        // //TODO [ERROR_FRAMEWORK] to think what to do
    }

    Controller.sessionID = null;
    Controller.userType = null;
    clientDialog.initialState();
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

    server = new RealServer(Settings.getInstance().getHost());

    processArgs(args);

    clientDialog = new ClientDialog();
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
        } catch (ServerReturnedNoAnswer srna) {
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
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, "При попытке обновить список контестов произошла ошибка: " + e.getMessage(), "Ошибка",
          JOptionPane.ERROR_MESSAGE);
      return new ContestDescription[]{};
    }
  }

  public static boolean addContest(ContestDescription cd) {
      try {
          CreateContestRequest contestRequest = new CreateContestRequest();
          contestRequest.sessionID = sessionID;
          contestRequest.contest = cd;
          server.doRequest(contestRequest);
      } catch (ServerReturnedError serverReturnedError) {
          //TODO [ERROR_FRAMEWORK] Log this error and check for inconsistency with error framework
          //JOptionPane.showMessageDialog(null, "failed to create a contest");
          return false;
      } catch (ServerReturnedNoAnswer serverReturnedNoAnswer) {
          //TODO [ERROR_FRAMEWORK] Log this error and check for inconsistency with error framework
          //JOptionPane.showMessageDialog(null, "failed to connect to the server");
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
        return null;
      } catch (ServerReturnedNoAnswer serverReturnedNoAnswer) {
        JOptionPane.showMessageDialog(null, "Сервер не отвечает");
        return null;
      }
    }

    public static boolean adjustContestData(AdjustContestRequest acr) {
        try {
          acr.sessionID = sessionID;
          server.doRequest(acr);
        } catch (ServerReturnedError serverReturnedError) {
            return false; //TODO create process of notifying a user of errors
        } catch (ServerReturnedNoAnswer serverReturnedNoAnswer) {
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
      JOptionPane.showMessageDialog(null, "Регистрация прошла успешно");
    } catch (ServerReturnedError serverReturnedError) {
      JOptionPane.showMessageDialog(null, "Регистрация не удалась. Ответ: " + serverReturnedError.getMessage());
    } catch (ServerReturnedNoAnswer serverReturnedNoAnswer) {
      JOptionPane.showMessageDialog(null, "Отсутствует соединение с сервером, попробуйте позже");
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
    return contestDescription.contestID;
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
    } catch (ServerReturnedNoAnswer e) {
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
            JOptionPane.showMessageDialog(null, "Сервер сообщает об ошибке: " + e.getMessage());
        } catch (ServerReturnedNoAnswer serverReturnedNoAnswer) {
            JOptionPane.showMessageDialog(null, "Отсутствует соединение с сервером, попробуйте позже");
        }

        return new UserDescription[]{};
    }

    public static ContestDescription getContestDescription() {
        return contestDescription;
    }

  public static void adjustClientPlugin(String alias, String description, File file) throws ServerReturnedError, ServerReturnedNoAnswer {
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
        throw new ServerReturnedError();
      }
    }
    else
      r.pluginData = null;

    server.doRequest(r);
  }

  public static void removeClientPlugin(String alias) throws ServerReturnedError, ServerReturnedNoAnswer {
    RemoveClientPluginRequest r = new RemoveClientPluginRequest();
    r.pluginAlias = alias;
    r.sessionID = sessionID;
    server.doRequest(r);
  }

    public static void addUser(String login, char[] password, String[] dataValue, UserDescription.UserType ut) throws ServerReturnedError, ServerReturnedNoAnswer {

        CreateUserRequest cur = new CreateUserRequest();

        UserDescription ud = new UserDescription();

        ud.login = login;
        ud.password = new String(password);
        ud.dataValue = dataValue;
        ud.userType = ut;

        cur.sessionID = sessionID;
        cur.user = ud;

        server.doRequest(cur);
    }

    public static void removeUser(int userID) throws ServerReturnedError, ServerReturnedNoAnswer {
        RemoveUserRequest rur = new RemoveUserRequest();

        rur.sessionID = sessionID;
        rur.userID = userID;

        server.doRequest(rur);
    }
}
