package ru.ipo.dces.mock;

import java.util.*;
import java.util.Map.Entry;

import ru.ipo.dces.client.ServerFacade;
import ru.ipo.dces.clientservercommunication.*;

public class MockServer implements ServerFacade {

  private final HashMap<Integer, ContestDescription> contestsList = new HashMap<Integer, ContestDescription>();
  private final HashMap<Integer, ProblemDescription> problemsList = new HashMap<Integer, ProblemDescription>();
  private final List<UserDescription>                usersList    = new ArrayList<UserDescription>();
  HashMap<String, SessionData>                       sessions     = new HashMap<String, SessionData>();

  private void CheckPassword(String sessionID, String password)
      throws RequestFailedResponse {
    if (sessions.get(sessionID) == null
        || sessions.get(sessionID).password != password)
      throw new RequestFailedResponse("Неверный пароль");
  }

  /** Настройка контеста */
  @Override
  public AcceptedResponse doRequest(AdjustContestRequest ad) throws Exception {
    // Если заданы новые параметры контеста => сначала меняем параметры контеста
    if (ad.contest != null)
      contestsList.put(ad.contest.contestID, ad.contest);
    contestsList.get(getSession(ad.sessionID).contestID);
    for (ProblemDescription problem : ad.problems)
      problemsList.put(problem.id, problem);
    return new AcceptedResponse();
  }

  @Override
  public AvailableContestsResponse doRequest(AvailableContestsRequest r) {
    AvailableContestsResponse res = new AvailableContestsResponse();
    ArrayList<ContestDescription> l = new ArrayList<ContestDescription>();
    for (Entry<Integer, ContestDescription> entry : contestsList.entrySet()) {
      if (!r.getInvisibleContests && !entry.getValue().visible)
        continue;
      l.add(entry.getValue());
    }
    res.contests = l.toArray(new ContestDescription[0]);
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
    sd.contestID = cc.contestID;
    sessions.put(sessionID, sd);
    ConnectToContestResponse res = new ConnectToContestResponse();
    res.sessionID = sessionID;
    return res;
  }

  @Override
  public AcceptedResponse doRequest(CreateContestRequest r) throws Exception {
    r.contest.contestID = contestsList.size();
    contestsList.put(r.contest.contestID, r.contest);
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
    getSession(disconnectRequest.sessionID);
    sessions.remove(disconnectRequest.sessionID);
    return new AcceptedResponse();
  }

  /** Получение подробных данных о контесте */
  @Override
  public GetContestDataResponse doRequest(GetContestDataRequest r)
      throws Exception {
    GetContestDataResponse res = new GetContestDataResponse();
    SessionData sd = getSession(r.sessionID);
    res.contest = contestsList.get(sd.contestID);
    // Получаем задачи с данным contestID
    ArrayList<ProblemDescription> pList = new ArrayList<ProblemDescription>();
    for (Entry<Integer, ProblemDescription> e : problemsList.entrySet())
      if (e.getValue().contestID == sd.contestID)
        pList.add(e.getValue());
    Collections.sort(pList);
    res.problems = pList.toArray(new ProblemDescription[0]);
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

  public AcceptedResponse doRequest(
      UploadClientPluginRequest uploadClientPluginRequest) {
    return new AcceptedResponse();
  }

  public void genData() throws Exception {
    doRequest(new CreateContestRequest("Contest #1"));
    doRequest(new CreateContestRequest("Contest #2"));
    doRequest(new CreateContestRequest("Contest #3"));
  }

  private SessionData getSession(String sessionID) throws RequestFailedResponse {
    SessionData sessionData = sessions.get(sessionID);
    if (sessionData == null)
      throw new RequestFailedResponse("Неверный sessionID");
    return sessionData;
  }

  private String getSessionID() {
    return UUID.randomUUID().toString();
  }
}
