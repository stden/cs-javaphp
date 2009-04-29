package ru.ipo.dces.debug;

import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;
import ru.ipo.dces.exceptions.ServerReturnedError;
import ru.ipo.dces.server.ServerFacade;
import ru.ipo.dces.client.LoggerFactory;

import java.util.HashMap;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 29.04.2009
 * Time: 14:45:05
 */
public class ServerPluginProxy implements ServerPluginEmulator {

  private ServerFacade server;
  private String sessionID;
  private int contestID;
  private String login;
  private String pass;
  private UserDescription.UserType userType;

  public ServerPluginProxy(ServerFacade server) {
    this.server = server;
    userType = UserDescription.UserType.Participant;
  }

  public ServerPluginProxy(ServerFacade server, String login, String pass, boolean isSuperAdmin) {
    this.server = server;
    this.login = login;
    this.pass = pass;
    if (isSuperAdmin)
      userType = UserDescription.UserType.SuperAdmin;
    else
      userType = UserDescription.UserType.ContestAdmin;
  }

  private void connectToContest(int contestID) throws GeneralRequestFailureException, ServerReturnedError {
    ConnectToContestRequest request = new ConnectToContestRequest();
    ConnectToContestResponse response;

    switch (userType) {
      case ContestAdmin:
      case SuperAdmin:
        request.contestID = contestID;
        request.login = login;
        request.password = pass;
        response = server.doRequest(request);
        break;
      case Participant:
        //first get contest description
        GetContestDataRequest r = new GetContestDataRequest();
        response = null;
        break;
      default:
        response = null;
    }

    sessionID = response.sessionID;
  }

  public void selectContest(int contestID) throws GeneralRequestFailureException, ServerReturnedError {
    connectToContest(contestID);
    this.contestID = contestID;
  }

  public void createContest(ContestDescription contest) throws GeneralRequestFailureException, ServerReturnedError {
    connectToContest(0);
  }

  public void adjustContest(ContestDescription contest) throws GeneralRequestFailureException {

  }

  public Object checkSolution(HashMap<String, String> solution, HashMap<String, String> result, Object state) {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public File getStatement() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }
}

/*
Full debug sequence
 1. copy .php to server
    (SuperAdmin)
 2. create contest with ContestData
    (SuperAdmin)
 2. create the problem with debugged client-plugin and server-plugin, submit statement and answer data
    (ContestAdmin)
 3. create an ordinary participant
    (ContestAdmin)
 4. connect as a participant, start test
 5. watch results as a participant / as an admin
 */

/* Server Plugin
*   1. use installed (Participant)
*   2. specify php (SuperAdmin)
*
*  Contest
*   1. use existing contest (Participant)
*   2. adjust existing contest (ContestAdmin)
*
*  Problem
*   1. user existing problem with no statement/answer change (Participant)
*   2. create one (ContestAdmin)
*
*  Participant
*   1. create new (ContestAdmin)
*   2. register by himself (Participant)
*/