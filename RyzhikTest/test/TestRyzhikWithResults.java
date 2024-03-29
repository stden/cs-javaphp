import ru.ipo.dces.debug.ServerPluginProxy;
import ru.ipo.dces.server.http.HttpServer;
import ru.ipo.dces.exceptions.ServerReturnedError;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;
import ru.ipo.dces.clientservercommunication.ContestDescription;
import ru.ipo.dces.clientservercommunication.UserDataField;
import ru.ipo.dces.clientservercommunication.ContestTiming;
import ru.ipo.dces.clientservercommunication.ResultsAccessPolicy;
import ru.ipo.dces.client.Controller;
import ru.ipo.dces.utils.FileSystemUtils;

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

  public static void main_create_initial_contest(String[] args) throws ServerReturnedError, GeneralRequestFailureException, IOException {
    HttpServer server = new HttpServer("http://vm-2.spb.ru/dces/dces.php");

    ServerPluginProxy problemProxy = new ServerPluginProxy(server, "admin", "pass", true);

    problemProxy.createContest(newDescription());

    for (int i = 0; i < 6; i++)
      if (i % 2 == 0)
        problemProxy.createProblem("RyzhikTest", "RyzhikChecker", new File("RyzhikTest/debug/1.gif"), new File("RyzhikTest/debug/1.txt"));
      else
        problemProxy.createProblem("RyzhikTest", "RyzhikChecker", new File("RyzhikTest/debug/2.gif"), new File("RyzhikTest/debug/2.txt"));

    problemProxy.createProblem("RyzhikResults", "EmptyPlugin", new File("RyzhikTest/debug/1.gif"), new File("RyzhikTest/debug/1.txt"));
  }

  public static void main(String[] args) throws IOException, ServerReturnedError, GeneralRequestFailureException {

    HttpServer server = new HttpServer("http://ipo.spb.ru/dces/test/dces.php");

    ServerPluginProxy problemProxy = new ServerPluginProxy(server, "admin", "pass", true);
    
    problemProxy.selectContest(4);

    problemProxy.uploadServerPlugin("RyzhikChecker", new File("RyzhikTest/debug/RyzhikChecker.php"));
    problemProxy.uploadServerPlugin("EmptyPlugin", new File("RyzhikResults/debug/EmptyPlugin.php"));

    //загрузить условия задач
    for (int i = 1; i <= 6; i++) {
      problemProxy.selectProblem(290 + i);
      problemProxy.adjustProblem(
              "RyzhikTest",
              "RyzhikChecker",
              new File("RyzhikTest/debug/" + i + ".gif"), //файл с картинкой
              new File("RyzhikTest/debug/" + i + ".txt")  //файл с ответом
      );

      FileSystemUtils.deleteDir(new File("problems/" + (290 + i)));
    }

    problemProxy.selectProblem(297);
    problemProxy.adjustProblem(
              "RyzhikResults",
              "EmptyPlugin",
              new File("RyzhikTest/debug/1.gif"),
              new File("RyzhikTest/debug/1.txt")
    );

    int pid = 0;
    for (int i = 72; i < 10000; i++) {
      try {
        problemProxy.newParticipant("p" + i, "pass");
      } catch (ServerReturnedError e) {
        if (e.getErrNo() == 17) continue;
        else throw e;
      }

      pid = i;
      break;
    }

    System.out.println("Заходите от пользователя p" + pid + " пароль pass");

    Controller.main(new String[0]);
  }

  private static ContestDescription newDescription() {
    ContestDescription res = new ContestDescription();

    Date now = new Date();

    UserDataField field1 = new UserDataField();
    field1.compulsory = true;
    field1.data = "Имя";
    field1.showInResult = true;
    UserDataField field2 = new UserDataField();
    field2.compulsory = false;
    field2.data = "Класс";
    field2.showInResult = false;

    res.contestTiming = new ContestTiming();
    res.contestID = -1;
    res.start = now;
    res.data = new UserDataField[]{field1, field2};
    res.description = "Пример соревнования на тесты рыжика. Регистрация по желанию";
    res.finish = new Date(now.getTime() + ((long)1000)*60*60*24*100); //100 days
    res.name = "Тесты Рыжика. Пример";
    res.registrationType = ContestDescription.RegistrationType.Self;
    res.resultsAccessPolicy = new ResultsAccessPolicy();

    res.contestTiming.selfContestStart = true;
    res.contestTiming.contestEndingStart = 0;
    res.contestTiming.contestEndingFinish = 0;
    res.contestTiming.maxContestDuration = 60;

    res.resultsAccessPolicy.afterContestPermission = ResultsAccessPolicy.AccessPermission.OnlySelfResults;
    res.resultsAccessPolicy.contestEndingPermission = ResultsAccessPolicy.AccessPermission.NoAccess;
    res.resultsAccessPolicy.contestPermission = ResultsAccessPolicy.AccessPermission.NoAccess;

    return res;
  }

}
