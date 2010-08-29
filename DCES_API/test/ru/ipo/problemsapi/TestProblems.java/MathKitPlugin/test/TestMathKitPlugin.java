import ru.ipo.dces.server.http.HttpServer;
import ru.ipo.dces.debug.ServerPluginProxy;
import ru.ipo.dces.debug.PluginBox;
import ru.ipo.dces.debug.ServerPluginEmulator;
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
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: I. Posov
 * Date: 11.05.2009
 * Time: 20:07:17
 */
public class TestMathKitPlugin {

  public static void main(String[] args) {

    ServerPluginEmulator spe = new ServerPluginEmulator() {
      public Object checkSolution(HashMap<String, String> solution, HashMap<String, String> result, Object state) throws GeneralRequestFailureException {
        System.out.println("SOLUTION = " + solution.toString());
        return null;
      }

      public File getStatement() throws GeneralRequestFailureException, IOException {
        return new File("MathKitPlugin/debug/c1");
      }
    };

    PluginBox pb = new PluginBox(PluginInt.class, spe);
    pb.setVisible(true);
    pb.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public static void main1(String[] args) throws ServerReturnedError, GeneralRequestFailureException, IOException {
    HttpServer server = new HttpServer("http://dces-server.ru:423/dces.php");

    ServerPluginProxy problemProxy = new ServerPluginProxy(server, "admin", "pass", true);
    problemProxy.selectContest(2);
    ContestDescription cd = newDescription();
    cd.resultsAccessPolicy.contestPermission = ResultsAccessPolicy.AccessPermission.OnlySelfResults;
    problemProxy.adjustContest(cd);
  }

  public static void main2(String[] args) throws ServerReturnedError, GeneralRequestFailureException, IOException {
    HttpServer server = new HttpServer("http://ipo.spb.ru/dces/test/dces.php");

    //connect to the server as a superadmin
    ServerPluginProxy problemProxy = new ServerPluginProxy(server, "admin", "pass", true);

    //create contest (unnecessary)
    //problemProxy.createContest(newDescription());

    //select the second contest (the contest created exactly for this debug)
    problemProxy.selectContest(3);

    //upload server plugin to the server. Skip this step, server plugin doesn't change
    //problemProxy.uploadServerPlugin("MathKitChecker", new File("MathKitPlugin/debug/MathKitChecker.php"));

    //upload server plugin to the server. Skip this step, will use local copy of the plugin
    //problemProxy.uploadClientPlugin("MathKitPlugin", new File("MathKitPlugin/MathKitPlugin.jar"));

    //set folder to store downloaded statement
    problemProxy.setStatementFolder(new File("MathKitPlugin/debug/debug-statement"));

    //create problem to debug, specify statement for the problem
    problemProxy.createProblem("MathKitPlugin", "MathKitChecker", new File("MathKitPlugin/debug/c1"), new File("MathKitPlugin/debug/empty-answer.txt"));

    //create new participant for the contest, debug in the name of this new participant
    problemProxy.newParticipant();

    //create plugin box to debug the plugin. There are two variants - debug local plugin or the downloaded plugin
    PluginBox pbox = new PluginBox(PluginInt.class, problemProxy);
    //PluginBox pbox = new PluginBox(problemProxy.getClientPlugin("MathKitPlugin"), problemProxy);

    //set up and show the plugin box
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
