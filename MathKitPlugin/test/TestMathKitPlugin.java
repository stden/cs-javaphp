import ru.ipo.dces.server.http.HttpServer;
import ru.ipo.dces.debug.ServerPluginProxy;
import ru.ipo.dces.debug.PluginBox;
import ru.ipo.dces.clientservercommunication.ContestDescription;
import ru.ipo.dces.clientservercommunication.ContestTiming;
import ru.ipo.dces.clientservercommunication.UserDataField;
import ru.ipo.dces.clientservercommunication.ResultsAccessPolicy;
import ru.ipo.dces.exceptions.ServerReturnedError;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;
import ru.ipo.dces.plugins.PluginInt;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: I. Posov
 * Date: 11.05.2009
 * Time: 20:07:17
 */
public class TestMathKitPlugin {

  public static void main(String[] args) throws ServerReturnedError, GeneralRequestFailureException, IOException {
    HttpServer server = new HttpServer("http://dces-server.ru:423/dces.php");

    ServerPluginProxy problemProxy = new ServerPluginProxy(server, "admin", "pass", true);
    problemProxy.createContest(newDescription());
    problemProxy.uploadServerPlugin("MathKitChecker", new File("MathKitPlugin/debug/MathKitChecker.php"));
    problemProxy.uploadClientPlugin("MathKitPlugin", new File("MathKitPlugin/MathKitPlugin.jar"));
    problemProxy.setStatementFolder(new File("MathKitPlugin/debug/debug-statement"));

    problemProxy.createProblem("MathKitPlugin", "MathKitChecker", new File("MathKitPlugin/debug/c1"), new File("MathKitPlugin/debug/empty-answer.txt"));

    problemProxy.newParticipant();

    //PluginBox pbox = new PluginBox(PluginInt.class, problemProxy);
    PluginBox pbox = new PluginBox(problemProxy.getClientPlugin("MathKitPlugin"), problemProxy);

    pbox.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pbox.setVisible(true);
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
