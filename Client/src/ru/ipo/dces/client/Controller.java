package ru.ipo.dces.client;

import javax.swing.*;

import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.plugins.*;
import ru.ipo.dces.plugins.admin.CreateContestPlugin;

/**
 * ����������, ������� ������ ������ � ���������� � �������� � ���������
 * ��������� ����������� �������� � ��������
 */
public class Controller {
  public static ServerFacade       server;
  public static String             sessionID;
  public static UserDescription    curUser;
  public static ContestDescription curContest;
  private static ClientDialog      clientDialog;

    /** ���������� Plugin'� � ������
   * @param pd the description of the problem for which the plugin is added*/
  public static void addPlugin(ProblemDescription pd) {
    PluginEnvironmentImpl pe = new PluginEnvironmentImpl(pd);

    Plugin p = PluginLoader.load(pd.clientPluginID, pe);

    clientDialog.addPluginToForm(pe, p);
  }

  public static void login(String login, char[] password) {
    try {
      ConnectToContestRequest request = new ConnectToContestRequest();
      //TODO get real contest ID
      request.contestID = 0;
      request.login = login;
      // TODO improve the security here
      request.password = new String(password);
      ConnectToContestResponse res = Controller.server.doRequest(request);
      Controller.sessionID = res.sessionID;

      // ������� ��� ���������� Plugin'�
      clientDialog.clearLeftPanel();

      // ���� ������������ ������������� ��� ������������� �������
      // ��������� ��� ���������������� Plugin'�
      switch (res.user.userType) {
        case ContestAdmin:
          PluginEnvironmentImpl ec = new PluginEnvironmentImpl(null);
          clientDialog.addPluginToForm(ec, new EditContestPlugin(ec));
          break;
        case SuperAdmin:
          switch (JOptionPane
              .showOptionDialog(
                  null,
                  "�� �������� ��� ������������� �������, �������� ��������, ������� ������ ���������",
                  "������������� �������", JOptionPane.DEFAULT_OPTION,
                  JOptionPane.QUESTION_MESSAGE, null, new String[] {
                      "��������� ������", "��������� �������", "������" }, null)) {
            case JOptionPane.CLOSED_OPTION:
            case 2:
              Controller.logout();
              break;
            case 0:
              PluginEnvironmentImpl ms = new PluginEnvironmentImpl(null);
              CreateContestPlugin serverPlugin = new CreateContestPlugin(ms);
              clientDialog.addPluginToForm(ms, serverPlugin);

              //test sample plugin
              PluginEnvironmentImpl spe = new PluginEnvironmentImpl(new ProblemDescription());
              Plugin sp = PluginLoader.load("SamplePlugin", spe);
              clientDialog.addPluginToForm(spe, sp);
              break;
            case 1:
              PluginEnvironmentImpl aec = new PluginEnvironmentImpl(null);
              clientDialog.addPluginToForm(aec, new EditContestPlugin(aec));
              break;
          }
          break;
        case Participant:
          // �������� ������ � �������
          GetContestDataRequest rq = new GetContestDataRequest();
          GetContestDataResponse rs = Controller.server.doRequest(rq);
          for (ProblemDescription pd : rs.problems)
            addPlugin(pd);
      }

      // ��������� Plugin ������ �� �������� � ����� �����
      PluginEnvironmentImpl pe = new PluginEnvironmentImpl(null);
      clientDialog.addPluginToForm(pe, new LogoutPlugin(pe));

    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, e.getMessage(), "������",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  /** ���������� ������ ������������ */
  public static void logout() {
    DisconnectRequest dr = new DisconnectRequest(sessionID);

      try {
          server.doRequest(dr);
      } catch (Exception serverReturnedError) {
          // TODO to think what to do
      }

      Controller.sessionID = null;
    clientDialog.initialState();
  }

  /**
   * ������ �������
   *
   * @param args the command line input
   */
  public static void main(String[] args) {
    server = new RealServer(Settings.getInstance().getHost());
    clientDialog = new ClientDialog();
    clientDialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    clientDialog.setVisible(true);
  }

  public static ContestDescription[] reloadContest() {
    try {
      AvailableContestsResponse res = Controller.server
          .doRequest(new AvailableContestsRequest());
      return res.contests;
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, e.getMessage(), "������",
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

}
