package ru.ipo.dces.client;

import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.plugins.EditContestPlugin;
import ru.ipo.dces.plugins.LogoutPlugin;
import ru.ipo.dces.plugins.admin.AdjustContestsPlugin;
import ru.ipo.dces.plugins.admin.CreateContestPlugin;

import javax.swing.*;
import java.util.Date;

/**
 * Контроллер, который хранит данные о соединении с сервером и позволяет
 * выполнять стандартные действия с сервером
 */
public class Controller {
  public static ServerFacade       server;
  public static String             sessionID;
  private static ClientDialog      clientDialog;

    /** Добавление Plugin'а в клиент
   * @param pd the description of the problem for which the plugin is added*/
  public static void addPlugin(ProblemDescription pd) {
    PluginEnvironmentImpl pe = new PluginEnvironmentImpl(pd);

    Plugin p = PluginLoader.load(pd.clientPluginAlias, pe);

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

      // Удаляем все запущенные Plugin'ы
      clientDialog.clearLeftPanel();

      // Если пользователь администратор или администратор сервера
      // загружаем ему административные Plugin'ы
      switch (res.user.userType) {
        case ContestAdmin:
          PluginEnvironmentImpl ec = new PluginEnvironmentImpl(null);
          clientDialog.addPluginToForm(ec, new EditContestPlugin(ec));
          break;
        case SuperAdmin:
          switch (JOptionPane
              .showOptionDialog(
                  null,
                  "Вы заходите как администратор сервера, выберите действия, которые хотите совершать",
                  "Администратор сервера", JOptionPane.DEFAULT_OPTION,
                  JOptionPane.QUESTION_MESSAGE, null, new String[] {
                      "Настроить сервер", "Настроить контест", "Отмена" }, null)) {
            case JOptionPane.CLOSED_OPTION:
            case 2:
              Controller.logout();
              break;
            case 0:
              PluginEnvironmentImpl ms1 = new PluginEnvironmentImpl(null);
              CreateContestPlugin ccp = new CreateContestPlugin(ms1);
              clientDialog.addPluginToForm(ms1, ccp);


              PluginEnvironmentImpl ms2 = new PluginEnvironmentImpl(null);
              AdjustContestsPlugin mcp = new AdjustContestsPlugin(ms2);
              clientDialog.addPluginToForm(ms2, mcp);

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
          // Получаем данные о задачах
          GetContestDataRequest rq = new GetContestDataRequest();
          GetContestDataResponse rs = Controller.server.doRequest(rq);
          for (ProblemDescription pd : rs.problems)
            addPlugin(pd);
      }

      // Добавляем Plugin выхода из контеста в самый конец
      PluginEnvironmentImpl pe = new PluginEnvironmentImpl(null);
      clientDialog.addPluginToForm(pe, new LogoutPlugin(pe));

    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  /** Завершение сессии пользователя */
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
   * Запуск клиента
   *
   * @param args the command line input
   */
  public static void main(String[] args) {
    server = new RealServer(Settings.getInstance().getHost());
    clientDialog = new ClientDialog();
    clientDialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    clientDialog.setVisible(true);
  }

  public static ContestDescription[] reloadContests() {
    try {
      AvailableContestsResponse res = Controller.server
          .doRequest(new AvailableContestsRequest());
      return res.contests;
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка",
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
        ProblemDescription pd1 = new ProblemDescription();
        pd1.answerData = null;
        pd1.clientPluginAlias = "SamplePlugin";
        pd1.id = 1;
        pd1.name = "ЗА УДАЧУ!";
        pd1.serverPluginAlias = "ComparePlugin";
        pd1.statement = null;
        pd1.statementData = null;

        ProblemDescription pd2 = new ProblemDescription();
        pd2.answerData = null;
        pd2.clientPluginAlias = "SamplePlugin2";
        pd2.id = 2;
        pd2.name = "ЗА УДАЧУ! 2";
        pd2.serverPluginAlias = "ComparePlugin2";
        pd2.statement = null;
        pd2.statementData = null;

        ProblemDescription[] pds = new ProblemDescription[]{pd1, pd2};

        ContestDescription cd = new ContestDescription();
        cd.contestID = contestID;
        cd.name = "Fake contest";
        cd.description = "Fake contest description";
        cd.start = new Date();
        cd.finish = new Date(new Date().getTime() + 1000 * 60 * 60);

        GetContestDataResponse response = new GetContestDataResponse();
        response.contest = cd;
        response.problems = pds;

        return response;
    }

    public static boolean adjustContestData(AdjustContestRequest acr) {
        //TODO: implement this method
        return false;
    }
}
