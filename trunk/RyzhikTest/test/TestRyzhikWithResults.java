import ru.ipo.dces.debug.PluginBox;
import ru.ipo.dces.debug.ServerPluginProxy;
import ru.ipo.dces.server.http.HttpServer;
import ru.ipo.dces.exceptions.ServerReturnedError;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;
import ru.ipo.dces.clientservercommunication.ContestDescription;
import ru.ipo.dces.clientservercommunication.UserDataField;
import ru.ipo.dces.clientservercommunication.ContestTiming;
import ru.ipo.dces.clientservercommunication.ResultsAccessPolicy;
import ru.ipo.dces.plugins.RyzhikPlugin;
import ru.ipo.dces.plugins.RyzhikResults;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: I. Posov
 * Date: 09.05.2009
 * Time: 15:36:16
 */
public class TestRyzhikWithResults {

  public static void main(String[] args) throws IOException, ServerReturnedError, GeneralRequestFailureException {

    HttpServer server = new HttpServer("http://dces-server.ru:423/dces.php");

    ServerPluginProxy problemProxy = new ServerPluginProxy(server, "admin", "pass", true);
    problemProxy.createContest(newDescription());
    problemProxy.uploadServerPlugin("RyzhikChecker", new File("RyzhikTest/debug/RyzhikChecker.php"));
    problemProxy.uploadServerPlugin("EmptyPlugin", new File("RyzhikResults/debug/EmptyPlugin.php"));
    int contestID = problemProxy.getContestID();
    File statementFolder = new File("RyzhikTest/debug/debug-statement");
    problemProxy.setStatementFolder(statementFolder);

    problemProxy.createProblem("", "RyzhikChecker", new File("RyzhikTest/debug/1.gif"), new File("RyzhikTest/debug/1.txt"));
    int problemID = problemProxy.getProblemID();
    problemProxy.createProblem("", "EmptyPlugin", new File("RyzhikTest/debug/1.gif"), new File("RyzhikTest/debug/1.txt"));
    int resultsID = problemProxy.getProblemID();

    problemProxy.selectProblem(problemID);

    problemProxy.newParticipant();
    String sessionID = problemProxy.getSessionID();

    ServerPluginProxy resultsProxy = new ServerPluginProxy(server, sessionID, contestID, resultsID);
    resultsProxy.setStatementFolder(statementFolder);    

    PluginBox pbProblem = new PluginBox(RyzhikPlugin.class, problemProxy);
    PluginBox pbResults = new PluginBox(RyzhikResults.class, resultsProxy);

    pbProblem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pbResults.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pbProblem.setVisible(true);
    pbResults.setVisible(true);
  }

  private static ContestDescription newDescription() {
    ContestDescription res = new ContestDescription();

    Date now = new Date();

    res.contestTiming = new ContestTiming();
    res.contestID = -1;
    res.start = now;
    res.data = new UserDataField[]{};
    res.description = "debug ryzhik contest";
    res.finish = new Date(now.getTime() + ((long)1000)*60*60*24*100); //100 days
    res.name = "Debug Ryzhik Contest";
    res.registrationType = ContestDescription.RegistrationType.ByAdmins;
    res.resultsAccessPolicy = new ResultsAccessPolicy();

    res.contestTiming.selfContestStart = true;
    res.contestTiming.contestEndingStart = 0;
    res.contestTiming.contestEndingFinish = 0;
    res.contestTiming.maxContestDuration = 10;

    res.resultsAccessPolicy.afterContestPermission = ResultsAccessPolicy.AccessPermission.OnlySelfResults;
    res.resultsAccessPolicy.contestEndingPermission = ResultsAccessPolicy.AccessPermission.NoAccess;
    res.resultsAccessPolicy.contestPermission = ResultsAccessPolicy.AccessPermission.NoAccess;

    return res;
  }

}
