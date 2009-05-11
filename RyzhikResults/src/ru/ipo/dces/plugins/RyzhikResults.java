package ru.ipo.dces.plugins;

import ru.ipo.dces.pluginapi.DCESPluginLoadable;
import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.pluginapi.PluginEnvironment;
import ru.ipo.dces.log.LogMessageType;
import ru.ipo.dces.server.ServerFacade;
import ru.ipo.dces.exceptions.ServerReturnedError;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;
import ru.ipo.dces.clientservercommunication.GetContestResultsRequest;
import ru.ipo.dces.clientservercommunication.GetContestResultsResponse;
import ru.ipo.dces.clientservercommunication.StopContestRequest;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: I. Posov
 * Date: 09.05.2009
 * Time: 13:43:54
 */
@DCESPluginLoadable
public class RyzhikResults implements Plugin {

  private JPanel mainPanel;
  private PluginEnvironment environment;
  private String[][] results;

  private enum InterfaceView {
    Nothing, NoResults, Results,
  }

  private InterfaceView interfaceView = InterfaceView.Nothing;

  public RyzhikResults(PluginEnvironment environment) {
    this.environment = environment;

    mainPanel = new JPanel();            
  }

  private InterfaceView getNeededInterfaceView() {
    ServerFacade server = environment.getServer();

    InterfaceView interfaceView;

    try {
      GetContestResultsRequest r = new GetContestResultsRequest();
      r.sessionID = environment.getSessionID();
      r.contestID = -1;
      GetContestResultsResponse response = server.doRequest(r);
      String[][] getResults = response.table[response.userLine];

      //remove admin and participant info if there is some

      int removeFirstColumns = response.headers[0].equals("admin info") ? 2 : 1;
      results = new String[getResults.length - removeFirstColumns - 1][]; //1 column is removed from the end
      System.arraycopy(getResults, removeFirstColumns, results, 0, results.length);
      interfaceView = InterfaceView.Results;
    } catch (ServerReturnedError ignored) {
      interfaceView = InterfaceView.NoResults;
    } catch (GeneralRequestFailureException ignored) {
      interfaceView = InterfaceView.NoResults;
    }
    return interfaceView;
  }

  private void setInterface(InterfaceView interfaceView) {

    if (this.interfaceView == interfaceView) return;

    mainPanel.removeAll();

    this.interfaceView = interfaceView;

    if (interfaceView == InterfaceView.Nothing) {
      environment.log("Ошибка в плагине оторажения результов, оратитесь к разработчикам", LogMessageType.Error);
      return;
    }

    if (interfaceView == InterfaceView.NoResults)
      setInterfaceDuringContest();
    else
      setInterfaceWithResults();    
  }

  private void setInterfaceWithResults() {
    //TODO нарисовать интферфейс с результатом
    //Метод getResultsTest() как бы выдает результаты (сейчас текстом)
    //mainPanel - панель, положить весь интерфейс на нее
    JLabel label = new JLabel(getResultsText());
    mainPanel.add(label);
  }

  private void setInterfaceDuringContest() {
    //TODO нарисовать инферфейс плагина результатов во время соревнования.
    //mainPanel - панель, положить весь интерфейс на неё
    //чтобы завершить соревнование, пользоваться методом stopContest();
    JLabel label = new JLabel("Результаты соревнования можно посмотреть только после окончания соревнования.");
    JButton button = new JButton("Завершить досрочно");
    mainPanel.add(label);
    mainPanel.add(button);

    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stopContest();
      }
    });
  }

  private String getResultsText() {
    //TODO use variable results and set text
    String ans = "";
    int i = 1;
    for (String[] result : results) {
      ans += i + ": " + Arrays.toString(result);
      i++;
    }
    return ans;
  }

  private void stopContest() {
    ServerFacade server = environment.getServer();
    StopContestRequest stopRequest = new StopContestRequest();
    stopRequest.contestID = -1;
    stopRequest.sessionID = environment.getSessionID();
    try {
      server.doRequest(stopRequest);
    } catch (Exception e) {
      environment.log("failed to stop contest: " + e.getMessage(), LogMessageType.Error);
    }

    setInterface(getNeededInterfaceView());
    mainPanel.repaint();
    mainPanel.doLayout();
  }

  public JPanel getPanel() {
    return mainPanel;
  }

  public void activate() {
    if (this.interfaceView == InterfaceView.Results) return;
    InterfaceView interfaceView = getNeededInterfaceView();
    setInterface(interfaceView);
  }

  public void deactivate() {
    //do nothing
  }
}