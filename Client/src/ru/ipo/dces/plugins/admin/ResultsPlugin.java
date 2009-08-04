package ru.ipo.dces.plugins.admin;

import ru.ipo.dces.client.Controller;
import ru.ipo.dces.client.Localization;
import ru.ipo.dces.client.ContestConnection;
import ru.ipo.dces.client.components.ContestChoosingPanel;
import ru.ipo.dces.clientservercommunication.ContestDescription;
import ru.ipo.dces.clientservercommunication.GetContestResultsRequest;
import ru.ipo.dces.clientservercommunication.GetContestResultsResponse;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;
import ru.ipo.dces.exceptions.ServerReturnedError;
import ru.ipo.dces.pluginapi.PluginEnvironment;
import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.plugins.admin.resultstable.ResultsTableModel;
import ru.ipo.dces.plugins.admin.resultstable.OneMessageTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import info.clearthought.layout.TableLayout;

/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 13.04.2009
 * Time: 0:39:44
 */
public class ResultsPlugin implements Plugin {

  private final JPanel mainPanel;
  private final JTable table;
  private final ContestChoosingPanel contestChoosingPanel;

  public ResultsPlugin(PluginEnvironment env) {
    this.mainPanel = new JPanel();
    this.table = new JTable();
    this.contestChoosingPanel = new ContestChoosingPanel();

    env.setTitle(Localization.getAdminPluginName(ResultsPlugin.class));

    JScrollPane scroll = new JScrollPane(table);
    mainPanel.setLayout(new TableLayout(new double[][]{
      {TableLayout.FILL}, {TableLayout.PREFERRED, TableLayout.FILL}
    }));
    mainPanel.add(scroll, "0, 1");
    table.setModel(new DefaultTableModel());

    contestChoosingPanel.addContestChangedActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        contestSelected(contestChoosingPanel.getContest());
      }
    });
    mainPanel.add(contestChoosingPanel, "0, 0");
    mainPanel.validate();
  }

  public JPanel getPanel() {
    return mainPanel;
  }

  public void activate() {
    contestChoosingPanel.setVisible(Controller.isContestUnknownMode());

    contestSelected(getContest());
  }

  private void showMessageInTable(String message) {    
    table.setModel(new OneMessageTableModel(message));
  }

  public void deactivate() {
    //do nothing
  }

  private ContestDescription getContest() {
    if (Controller.isContestUnknownMode())
      return contestChoosingPanel.getContest();
    else
      return Controller.getContestConnection().getContest();
  }

  private void contestSelected(ContestDescription contest) {
    if (contest == null) {
      showMessageInTable("Выберите соревнование");
      return;
    }

    try {
      GetContestResultsRequest crr = new GetContestResultsRequest();
      crr.contestID = contest.contestID;
      ContestConnection contestConnection = Controller.getContestConnection();
      crr.sessionID = Controller.getContestConnection() == null ?
              null :
              contestConnection.getSessionID();
      GetContestResultsResponse r = Controller.getServer().doRequest(crr);

      ResultsTableModel model = new ResultsTableModel(r.headers, r.minorHeaders, r.table);
      table.setModel(model);
    } catch (ServerReturnedError e) {
      showMessageInTable(e.getMessage());
    } catch (GeneralRequestFailureException e) {
      //do nothing
    }
  }

}
