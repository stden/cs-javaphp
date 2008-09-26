package ru.ipo.dces.mock;

import java.util.*;

import ru.ipo.dces.client.IServer;
import ru.ipo.dces.clientservercommunication.*;

public class MockServer implements IServer {

  private final List<ContestDescription> contestsList = new ArrayList<ContestDescription>();
  private final List<UserDescription>    usersList    = new ArrayList<UserDescription>();
  HashMap<String, SessionData>           sessions     = new HashMap<String, SessionData>();

  private void CheckPassword(String sessionID, String password)
      throws RequestFailedResponse {
    if (sessions.get(sessionID) == null
        || sessions.get(sessionID).password != password)
      throw new RequestFailedResponse("Неверный пароль");
  }

  @Override
  public AcceptedResponse doRequest(AdjustContestRequest ad) {
    return new AcceptedResponse();
  }

  @Override
  public AvailableContestsResponse doRequest(AvailableContestsRequest acr) {
    AvailableContestsResponse res = new AvailableContestsResponse();
    res.contests = contestsList.toArray(new ContestDescription[0]);
    return res;
  }

  @Override
  public AcceptedResponse doRequest(ChangePasswordRequest r) throws Exception {
    CheckPassword(r.sessionID, r.oldPassword);
    return new AcceptedResponse();
  }

  @Override
  public ConnectToContestResponse doRequest(ConnectToContestRequest cc)
      throws Exception {
    boolean found = false;
    for (UserDescription user : usersList)
      if (user.login == cc.login && user.password == cc.password) {
        found = true;
        break;
      }
    if (!found)
      throw new RequestFailedResponse("Неверный логин или пароль");

    String sessionID = getSessionID();
    SessionData sd = new SessionData();
    sd.login = cc.login;
    sd.password = cc.password;
    sessions.put(sessionID, sd);
    ConnectToContestResponse res = new ConnectToContestResponse();
    res.sessionID = sessionID;
    return res;
  }

  @Override
  public AcceptedResponse doRequest(CreateContestRequest createContestRequest)
      throws Exception {
    contestsList.add(createContestRequest.contest);
    return new AcceptedResponse();
  }

  @Override
  public AcceptedResponse doRequest(CreateUserRequest cur) throws Exception {
    usersList.add(cur.user);
    return new AcceptedResponse();
  }

  @Override
  public AcceptedResponse doRequest(DisconnectRequest disconnectRequest)
      throws Exception {
    if (sessions.get(disconnectRequest.sessionID) == null)
      throw new RequestFailedResponse("Неверный sessionID");
    sessions.remove(disconnectRequest.sessionID);
    return new AcceptedResponse();
  }

  @Override
  public GetContestDataResponse doRequest(GetContestDataRequest gc)
      throws Exception {
    GetContestDataResponse res = new GetContestDataResponse();
    return res;
  }

  @Override
  public GetUsersResponse doRequest(GetUsersRequest gur) throws Exception {
    GetUsersResponse getUsersResponse = new GetUsersResponse();
    getUsersResponse.users = usersList.toArray(new UserDescription[0]);
    return getUsersResponse;
  }

  @Override
  public InstallClientPluginResponse doRequest(
      InstallClientPluginRequest installClientPluginRequest) {
    return new InstallClientPluginResponse();
  }

  @Override
  public AcceptedResponse doRequest(
      RegisterToContestRequest registerToContestRequest) {
    return new AcceptedResponse();
  }

  @Override
  public AcceptedResponse doRequest(
      RemoveClientPluginRequest removeClientPluginRequest) {
    return new AcceptedResponse();
  }

  @Override
  public AcceptedResponse doRequest(
      RestorePasswordRequest restorePasswordRequest) throws Exception {
    return new AcceptedResponse();
  }

  private String getSessionID() {
    return UUID.randomUUID().toString();
  }
}
