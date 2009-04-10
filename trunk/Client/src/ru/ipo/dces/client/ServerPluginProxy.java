package ru.ipo.dces.client;

import ru.ipo.dces.debug.ServerPluginEmulator;
import ru.ipo.dces.clientservercommunication.*;

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
//TODO add throws to ServerPluginEmulator methods and remove return null from this class
public class ServerPluginProxy implements ServerPluginEmulator {

  private final ProblemDescription problem;

  public ServerPluginProxy(ProblemDescription problem) {
    this.problem = problem;
  }

  public Object checkSolution(HashMap<String, String> solution, HashMap<String, String> result, Object state) {
    final SubmitSolutionRequest solutionRequest = new SubmitSolutionRequest();
    solutionRequest.problemID = problem.id;
    solutionRequest.problemResult = solution;
    solutionRequest.sessionID = Controller.getSessionID();
    SubmitSolutionResponse r;
    try {
      r = Controller.getServer().doRequest(solutionRequest);
    } catch (Exception e) {
      System.out.println("DEBUD error: failed to checkSolution");
      e.printStackTrace();
      return null;
    }

    for (Map.Entry<String, String> k2v : r.problemResult.entrySet())
      result.put(k2v.getKey(), k2v.getValue());
    return null; //don't using state
  }

  public File getStatement() {
    //TODO clear folder first

    final File folder = Controller.getProblemDebugDirectoryByID(problem.id);
    try {
      Controller.unzip(problem.statement, folder);
    } catch (IOException e) {
      System.out.println("DEBUG error, failed to unzip:");
      e.printStackTrace();
    }

    return folder;
  }
}