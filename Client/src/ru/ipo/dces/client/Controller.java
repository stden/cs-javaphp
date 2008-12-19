package ru.ipo.dces.client;

import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.debug.PluginBox;
import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.pluginapi.PluginEnvironment;
import ru.ipo.dces.plugins.admin.AdjustContestsPlugin;
import ru.ipo.dces.plugins.admin.CreateContestPlugin;
import ru.ipo.dces.plugins.admin.LogoutPlugin;
import ru.ipo.dces.plugins.admin.ManageUsersPlugin;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.Constructor;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * ����������, ������� ������ ������ � ���������� � �������� � ���������
 * ��������� ����������� �������� � ��������
 */
public class Controller {
  private static ServerFacade       server;
  private static String             sessionID;
  private static int                contestID;
  private static ContestDescription contestDescription;
  private static ClientDialog       clientDialog;
  private static UserDescription.UserType userType;

  /** ���������� Plugin'� � ������
   * @param pd the description of the problem for which the plugin is added
   */
  public static void addPlugin(ProblemDescription pd) {
    PluginEnvironmentImpl pe = new PluginEnvironmentImpl(pd);

    Plugin p = PluginLoader.load(pd.clientPluginAlias, pe);

    clientDialog.addPluginToForm(pe.getView(), p);
  }

  /** ���������� Plugin'� � ������
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

  public static void login(String login, char[] password, int contestID) {
    try {
      ConnectToContestRequest request = new ConnectToContestRequest();
      request.contestID = contestID;
      Controller.contestID = contestID;
      request.login = login;
      // TODO improve the security here
      request.password = new String(password);
      ConnectToContestResponse res = Controller.server.doRequest(request);
      Controller.sessionID = res.sessionID;
      //TODO save contest description in 'contestDescription'

      // ������� ��� ���������� Plugin'�
      clientDialog.clearLeftPanel();

      // ���� ������������ ������������� ��� ������������� �������
      // ��������� ��� ���������������� Plugin'�
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
          addAdminPlugin(LogoutPlugin.class);
          break;
        case Participant:
          // �������� ������ � �������
          refreshParticipantInfo();
      }
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, "��� ������� ����������� � �������� ��������� ������: " + e.getMessage(), "������",
          JOptionPane.ERROR_MESSAGE);
      clientDialog.initialState();
    }
  }

  //TODO add param 'refresh' and if false don't download statements that already are on disk
  public static void refreshParticipantInfo() throws ServerReturnedError, ServerReturnedNoAnswer, IOException {
    GetContestDataRequest rq = new GetContestDataRequest();
    rq.contestID = -1;
    rq.infoType = GetContestDataRequest.InformationType.ParticipantInfo;
    rq.extendedData = null;
    rq.sessionID = sessionID;
    GetContestDataResponse rs = Controller.server.doRequest(rq);
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

  /** ���������� ������ ������������ */
  public static void logout() {
    DisconnectRequest dr = new DisconnectRequest();
    dr.sessionID = sessionID;

    try {
        server.doRequest(dr);
        Controller.contestID = -1;
    } catch (Exception serverReturnedError) {
        // TODO to think what to do
    }

    Controller.sessionID = null;
    Controller.userType = null;
    clientDialog.initialState();
  }

  /**
   * ������ �������
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
      JOptionPane.showMessageDialog(null, "��� ������� �������� ������ ��������� ��������� ������: " + e.getMessage(), "������",
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
          JOptionPane.showMessageDialog(null, "failed to create a contest");
          return false;
      } catch (ServerReturnedNoAnswer serverReturnedNoAnswer) {
          JOptionPane.showMessageDialog(null, "failed to connect to the server");
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
        JOptionPane.showMessageDialog(null, "������ �� ��������");
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
      JOptionPane.showMessageDialog(null, "����������� ������ �������");
    } catch (ServerReturnedError serverReturnedError) {
      JOptionPane.showMessageDialog(null, "����������� �� �������. �����: " + serverReturnedError.getMessage());
    } catch (ServerReturnedNoAnswer serverReturnedNoAnswer) {
      JOptionPane.showMessageDialog(null, "����������� ���������� � ��������, ���������� �����");
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
    return contestID;
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
            JOptionPane.showMessageDialog(null, "������ �������� �� ������: " + e.getMessage());
        } catch (ServerReturnedNoAnswer serverReturnedNoAnswer) {
            JOptionPane.showMessageDialog(null, "����������� ���������� � ��������, ���������� �����");
        }

        return new UserDescription[]{};
    }

    public static ContestDescription getContestDescription() {
        return contestDescription;
    }
}
