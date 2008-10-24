package ru.ipo.dces.client;

import javax.swing.*;

import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.mock.MockServer;
import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.plugins.*;

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

  /** ���������� Plugin'� � ������ */
  public static void addPlugin(ProblemDescription pd) {
    PluginEnvironmentImpl pe = new PluginEnvironmentImpl(pd);

    Plugin p = PluginLoader.load(pd.clientPluginID, pe);

    clientDialog.addPluginToForm(pe, p);
  }

  public static void login(String login, char[] password) {
    try {
      ConnectToContestRequest request = new ConnectToContestRequest();
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
              clientDialog.addPluginToForm(ms, new ManageServerPlugin(ms));
              break;
            case 1:
              PluginEnvironmentImpl aec = new PluginEnvironmentImpl(null);
              clientDialog.addPluginToForm(aec, new EditContestPlugin(aec));
              break;
          }
          break;
        case User:
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
    } catch (RequestFailedResponse e) {
      // do nothing
      // TODO ��������, ��� ������ �� ����� ����
    }
    Controller.sessionID = null;
    clientDialog.initialState();
  }

  /**
   * ������ �������
   * 
   * @param args
   */
  public static void main(String[] args) {
    MockServer ms = new MockServer();
    try {
      ms.genMockData();
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, e.getMessage(), "������",
          JOptionPane.ERROR_MESSAGE);
    }

    server = ms;
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
      return null;
    }
  }

}
