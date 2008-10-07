package ru.ipo.dces.mock;

import java.util.*;
import java.util.Map.Entry;

import ru.ipo.dces.client.ServerFacade;
import ru.ipo.dces.clientservercommunication.*;

public class MockServer implements ServerFacade {

  private final HashMap<Integer, ContestDescription> contests = new HashMap<Integer, ContestDescription>();
  private final HashMap<Integer, ProblemDescription> problems = new HashMap<Integer, ProblemDescription>();
  private final HashMap<String, UserDescription>     users    = new HashMap<String, UserDescription>();
  HashMap<String, SessionServer>                     sessions = new HashMap<String, SessionServer>();

  /** Начальное заполнение БД */
  public MockServer() {
    UserDescription admin = new UserDescription();
    // Добавляем первого администратора
    admin.isAdmin = true;
    admin.login = "admin";
    admin.password = "adminpass";
    users.put(admin.login, admin);
  }

  private void CheckPassword(String sessionID, String password)
      throws RequestFailedResponse {
    if (sessions.get(sessionID) == null
        || users.get(sessions.get(sessionID).login).password != password)
      throw new RequestFailedResponse("Неверный пароль");
  }

  /** Настройка контеста */
  @Override
  public AcceptedResponse doRequest(AdjustContestRequest r) throws Exception {
    SessionServer session = getSession(r.sessionID);
    // Если заданы новые параметры контеста => сначала меняем параметры контеста
    if (r.contest != null)
      contests.put(r.contest.contestID, r.contest);
    contests.get(session.contestID);
    for (ProblemDescription problem : r.problems)
      problems.put(problem.id, problem);
    return new AcceptedResponse();
  }

  @Override
  public AvailableContestsResponse doRequest(AvailableContestsRequest r) {
    AvailableContestsResponse res = new AvailableContestsResponse();
    ArrayList<ContestDescription> l = new ArrayList<ContestDescription>();
    for (Entry<Integer, ContestDescription> entry : contests.entrySet()) {
      if (!r.getInvisibleContests && !entry.getValue().visible)
        continue;
      l.add(entry.getValue());
    }
    res.contests = l.toArray(new ContestDescription[0]);
    return res;
  }

  @Override
  public AcceptedResponse doRequest(ChangePasswordRequest r) throws Exception {
    getSession(r.sessionID);
    CheckPassword(r.sessionID, r.oldPassword);
    return new AcceptedResponse();
  }

  @Override
  public ConnectToContestResponse doRequest(ConnectToContestRequest r)
      throws Exception {
    UserDescription user = users.get(r.login);
    if (user == null || user.password != r.password)
      throw new RequestFailedResponse("Неверный логин или пароль");

    String sessionID = getSessionID();
    SessionServer sd = new SessionServer(user.login, r.contestID);
    sd.login = r.login;
    sd.contestID = r.contestID;
    sessions.put(sessionID, sd);
    ConnectToContestResponse res = new ConnectToContestResponse();
    res.sessionID = sessionID;
    return res;
  }

  @Override
  public AcceptedResponse doRequest(CreateContestRequest r) throws Exception {
    getSession(r.sessionID);
    r.contest.contestID = contests.size();
    contests.put(r.contest.contestID, r.contest);
    return new AcceptedResponse();
  }

  @Override
  public AcceptedResponse doRequest(CreateUserRequest r) throws Exception {
    getSession(r.sessionID);
    users.put(r.user.login, r.user);
    return new AcceptedResponse();
  }

  @Override
  public AcceptedResponse doRequest(DisconnectRequest r) throws Exception {
    getSession(r.sessionID);
    sessions.remove(r.sessionID);
    return new AcceptedResponse();
  }

  /** Получение подробных данных о контесте */
  @Override
  public GetContestDataResponse doRequest(GetContestDataRequest r)
      throws Exception {
    GetContestDataResponse res = new GetContestDataResponse();
    SessionServer sd = getSession(r.sessionID);
    res.contest = contests.get(sd.contestID);
    // Получаем задачи с данным contestID
    ArrayList<ProblemDescription> pList = new ArrayList<ProblemDescription>();
    for (Entry<Integer, ProblemDescription> e : problems.entrySet())
      if (e.getValue().contestID == sd.contestID)
        pList.add(e.getValue());
    Collections.sort(pList);
    res.problems = pList.toArray(new ProblemDescription[0]);
    return res;
  }

  /** Получение списка всех пользователей, которые в том же контесте */
  @Override
  public GetUsersResponse doRequest(GetUsersRequest gur) throws Exception {
    SessionServer session = getSession(gur.sessionID);
    // if (!session.isAdmin)
    // throw new RequestFailedResponse("Требуются права администратора");

    GetUsersResponse getUsersResponse = new GetUsersResponse();
    ArrayList<UserDescription> uList = new ArrayList<UserDescription>();
    for (Entry<String, SessionServer> s : sessions.entrySet())
      if (s.getValue().contestID == session.contestID)
        uList.add(users.get(s.getValue().login));

    getUsersResponse.users = uList.toArray(new UserDescription[0]);
    return getUsersResponse;
  }

  @Override
  public InstallClientPluginResponse doRequest(InstallClientPluginRequest r)
      throws Exception {
    getSession(r.sessionID);

    return new InstallClientPluginResponse();
  }

  @Override
  public AcceptedResponse doRequest(RegisterToContestRequest r) {

    return new AcceptedResponse();
  }

  @Override
  public AcceptedResponse doRequest(RemoveClientPluginRequest r)
      throws Exception {
    getSession(r.sessionID);

    return new AcceptedResponse();
  }

  @Override
  public AcceptedResponse doRequest(RestorePasswordRequest r) throws Exception {

    return new AcceptedResponse();
  }

  public SubmitSolutionResponse doRequest(SubmitSolutionRequest r)
      throws Exception {
    getSession(r.sessionID);
    SubmitSolutionResponse res = new SubmitSolutionResponse();
    return res;
  }

  public AcceptedResponse doRequest(UploadClientPluginRequest r)
      throws RequestFailedResponse {
    getSession(r.sessionID);

    return new AcceptedResponse();
  }

  public void genData() throws Exception {
    doRequest(new CreateContestRequest("Contest #1"));
    doRequest(new CreateContestRequest("Contest #2"));
    doRequest(new CreateContestRequest("Contest #3"));
  }

  private SessionServer getSession(String sessionID)
      throws RequestFailedResponse {
    SessionServer sessionData = sessions.get(sessionID);
    if (sessionData == null)
      throw new RequestFailedResponse("Неверный sessionID");
    return sessionData;
  }

  private String getSessionID() {
    return UUID.randomUUID().toString();
  }
}
