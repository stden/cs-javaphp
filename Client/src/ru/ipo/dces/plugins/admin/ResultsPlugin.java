package ru.ipo.dces.plugins.admin;

import ru.ipo.dces.client.AdminPlugin;
import ru.ipo.dces.client.Controller;
import ru.ipo.dces.client.Localization;
import ru.ipo.dces.clientservercommunication.ContestDescription;
import ru.ipo.dces.clientservercommunication.GetContestResultsRequest;
import ru.ipo.dces.clientservercommunication.GetContestResultsResponse;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;
import ru.ipo.dces.exceptions.ServerReturnedError;
import ru.ipo.dces.pluginapi.PluginEnvironment;
import ru.ipo.dces.plugins.admin.resultstable.ResultsTableModel;
import ru.ipo.dces.plugins.admin.resultstable.OneMessageTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 13.04.2009
 * Time: 0:39:44
 */
public class ResultsPlugin implements AdminPlugin {

  private final JPanel mainPanel;
  private final JTable table;

  public ResultsPlugin(PluginEnvironment env) {
    this.mainPanel = new JPanel();
    this.table = new JTable();

    env.setTitle(Localization.getAdminPluginName(ResultsPlugin.class));

    JScrollPane scroll = new JScrollPane(table);
    mainPanel.add(scroll);
    table.setModel(new DefaultTableModel());
  }

  public JPanel getPanel() {
    return mainPanel;
  }

  public void activate() {
    //do nothing
  }

  private void showMessageInTable(String message) {    
    table.setModel(new OneMessageTableModel(message));
  }

  public void deactivate() {
    //do nothing
  }

  public void contestSelected(ContestDescription contest) {
    if (contest == null) {
      showMessageInTable("Выберите соревнование");
      return;
    }

    try {
      GetContestResultsRequest crr = new GetContestResultsRequest();
      crr.contestID = contest.contestID;
      crr.sessionID = Controller.getSessionID();
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
