package ru.ipo.dces.client;

import ru.ipo.dces.debug.ServerPluginEmulator;
import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.utils.ZipUtils;
import ru.ipo.dces.exceptions.ServerReturnedError;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;
import ru.ipo.dces.log.LoggerFactory;
import ru.ipo.dces.log.LogMessageType;

import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Илья
 * Date: 17.12.2008
 * Time: 13:50:33
 */
public class ServerPluginProxy implements ServerPluginEmulator {

  private final ProblemDescription problem;

  public ServerPluginProxy(ProblemDescription problem) {
    this.problem = problem;
  }

  public Object checkSolution(HashMap<String, String> solution, HashMap<String, String> result, Object state) throws GeneralRequestFailureException {
    final SubmitSolutionRequest solutionRequest = new SubmitSolutionRequest();
    solutionRequest.problemID = problem.id;
    solutionRequest.problemResult = solution;
    solutionRequest.sessionID = Controller.getSessionID();
    SubmitSolutionResponse r;

    try {
      r = Controller.getServer().doRequest(solutionRequest);
    } catch (ServerReturnedError serverReturnedError) {
      LoggerFactory.getLogger().log(serverReturnedError.getMessage(), LogMessageType.Error, null);
      throw new GeneralRequestFailureException();
    }

    for (Map.Entry<String, String> k2v : r.problemResult.entrySet())
      result.put(k2v.getKey(), k2v.getValue());
    return null; //don't using state
  }

  public File getStatement() throws IOException {
    //TODO clear folder first

    final File folder = Controller.getProblemDebugDirectoryByID(problem.id);

    ZipUtils.unzip(problem.statement, folder);    

    return folder;
  }
}