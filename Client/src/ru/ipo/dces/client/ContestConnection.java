package ru.ipo.dces.client;

import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.server.ServerFacade;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;
import ru.ipo.dces.exceptions.ServerReturnedError;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ilya
 * Date: 24.07.2009
 * Time: 19:17:43
 */
public class ContestConnection {

  private String sessionID;
  private ContestDescription contest;
  private ServerFacade server;
  private UserDescription user;
  private Date finishTime;

  public ContestConnection(ServerFacade server,
                           ContestDescription contestDescription,
                           String login,
                           char[] password
  ) throws GeneralRequestFailureException, ServerReturnedError {
    boolean superAdmin = contestDescription == null;
    this.server = server;

    int contestID = superAdmin ? 0 : contestDescription.contestID;
    ConnectToContestRequest request = new ConnectToContestRequest();
    request.contestID = contestID;
    request.login = login;
    //TODO improve the security here
    request.password = new String(password);
    ConnectToContestResponse res = server.doRequest(request);

    sessionID = res.sessionID;
    user = res.user;
    contest = contestDescription;
    finishTime = res.finishTime;
  }

  public ContestConnection(ServerFacade server,
                           String login,
                           char[] password
  ) throws GeneralRequestFailureException, ServerReturnedError {
    this(server, null, login, password);
  }

  /*ContestConnection(ServerFacade server, ContestDescription contest, String sessionID) {
    this.server = server;
    this.contest = contest;
    this.sessionID = sessionID;

    //TODO to think of this values
    this.user = null;
    this.finishTime = null;
  }*/

  public void disconnect() throws GeneralRequestFailureException, ServerReturnedError {
    DisconnectRequest dr = new DisconnectRequest();
    dr.sessionID = sessionID;

    server.doRequest(dr);

    sessionID = null;
    user = null;
    contest = null;
  }

  public String getSessionID() {
    return sessionID;
  }

  public UserDescription getUser() {
    return user;
  }

  public ContestDescription getContest() {
    return contest;
  }

  public ServerFacade getServer() {
    return server;
  }

  public Date getFinishTime() {
    return finishTime;
  }

}
