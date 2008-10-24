package ru.ipo.dces.mock;

import java.util.*;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import ru.ipo.dces.client.*;
import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.clientservercommunication.UserDescription.UserType;

public class MockServer implements ServerFacade {

  private static final String                        rootLogin       = "admin";
  private static final String                        rootPassword    = "adminpass";

  private final HashMap<Integer, ContestDescription> contests        = new HashMap<Integer, ContestDescription>();
  private final HashMap<Integer, ProblemDescription> problems        = new HashMap<Integer, ProblemDescription>();
  private final HashMap<Integer, Integer>            problem2contest = new HashMap<Integer, Integer>();
  private final HashMap<String, UserDescription>     users           = new HashMap<String, UserDescription>();
  HashMap<String, MockSessionServer>                 sessions        = new HashMap<String, MockSessionServer>();

  /** Начальное заполнение БД */
  public MockServer() {
    UserDescription admin = new UserDescription();
    // Добавляем первого администратора
    admin.userType = UserType.SuperAdmin;
    admin.login = rootLogin;
    admin.password = rootPassword;
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
  public AcceptedResponse doRequest(AdjustContestRequest r)
      throws RequestFailedResponse {
    MockSessionServer session = getSession(r.sessionID);
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
  public AcceptedResponse doRequest(ChangePasswordRequest r)
      throws RequestFailedResponse {
    getSession(r.sessionID);
    CheckPassword(r.sessionID, r.oldPassword);
    return new AcceptedResponse();
  }

  /** Присоединиться к контесту или к просто к серверу для создания контестов */
  @Override
  public ConnectToContestResponse doRequest(ConnectToContestRequest r)
      throws RequestFailedResponse {
    // Проверяем логин и пароль пользователя
    // Для этого получаем пользователя по login'у
    UserDescription user = users.get(r.login);
    // и проверяем его пароль
    if (user == null || !user.password.equals(r.password))
      throw new RequestFailedResponse("Неверный логин или пароль");

    // Если пароль и логин правильные - открываем сессию
    String sessionID = generateSessionID();
    // Сохраняем информацию о сессии на сервере
    MockSessionServer sd = new MockSessionServer(user.login, r.contestID);
    sd.login = r.login;
    sd.contestID = r.contestID;
    sessions.put(sessionID, sd);
    // Формируем ответ клиенту
    ConnectToContestResponse res = new ConnectToContestResponse();
    res.sessionID = sessionID;
    res.user = user;
    return res;
  }

  /** Создание нового контеста */
  @Override
  public AcceptedResponse doRequest(CreateContestRequest r)
      throws RequestFailedResponse {
    getSession(r.sessionID);
    r.contest.contestID = contests.size();
    contests.put(r.contest.contestID, r.contest);
    return new AcceptedResponse();
  }

  /** Создание нового пользователя */
  @Override
  public AcceptedResponse doRequest(CreateUserRequest r)
      throws RequestFailedResponse {
    getSession(r.sessionID);
    users.put(r.user.login, r.user);
    return new AcceptedResponse();
  }

  @Override
  public AcceptedResponse doRequest(DisconnectRequest r)
      throws RequestFailedResponse {
    getSession(r.sessionID);
    sessions.remove(r.sessionID);
    return new AcceptedResponse();
  }

  /** Получение подробных данных о контесте */
  @Override
  public GetContestDataResponse doRequest(GetContestDataRequest r)
      throws RequestFailedResponse {
    GetContestDataResponse res = new GetContestDataResponse();
    MockSessionServer sd = getSession(r.sessionID);
    res.contest = contests.get(sd.contestID);
    // Получаем задачи с данным contestID
    ArrayList<ProblemDescription> pList = new ArrayList<ProblemDescription>();
    // Пробегаем по списку соответствия задача-контест
    for (Entry<Integer, Integer> p : problem2contest.entrySet())
      if (p.getValue() == sd.contestID) // Если у задачи заданный контест
        pList.add(problems.get(p.getKey())); // то добавляем эту задачу в список
    Collections.sort(pList);
    res.problems = pList.toArray(new ProblemDescription[0]);
    return res;
  }

  /** Получение списка всех пользователей, которые в том же контесте */
  @Override
  public GetUsersResponse doRequest(GetUsersRequest gur)
      throws RequestFailedResponse {
    MockSessionServer session = getSession(gur.sessionID);
    // if (!session.isAdmin)
    // throw new RequestFailedResponse("Требуются права администратора");

    GetUsersResponse getUsersResponse = new GetUsersResponse();
    ArrayList<UserDescription> uList = new ArrayList<UserDescription>();
    for (Entry<String, MockSessionServer> s : sessions.entrySet())
      if (s.getValue().contestID == session.contestID)
        uList.add(users.get(s.getValue().login));

    getUsersResponse.users = uList.toArray(new UserDescription[0]);
    return getUsersResponse;
  }

  @Override
  public InstallClientPluginResponse doRequest(InstallClientPluginRequest r)
      throws RequestFailedResponse {
    getSession(r.sessionID);

    return new InstallClientPluginResponse();
  }

  @Override
  public AcceptedResponse doRequest(RegisterToContestRequest r) {

    return new AcceptedResponse();
  }

  @Override
  public AcceptedResponse doRequest(RemoveClientPluginRequest r)
      throws RequestFailedResponse {
    getSession(r.sessionID);

    return new AcceptedResponse();
  }

  @Override
  public AcceptedResponse doRequest(RestorePasswordRequest r)
      throws RequestFailedResponse {

    return new AcceptedResponse();
  }

  public SubmitSolutionResponse doRequest(SubmitSolutionRequest r)
      throws RequestFailedResponse {
    getSession(r.sessionID);
    JOptionPane.showMessageDialog(null, r.problemResult.toString(),
        "Сообщение", JOptionPane.INFORMATION_MESSAGE);
    SubmitSolutionResponse res = new SubmitSolutionResponse();
    // TODO: реализовать вызов плагина стороны сервера
    return res;
  }

  public AcceptedResponse doRequest(UploadClientPluginRequest r)
      throws RequestFailedResponse {
    getSession(r.sessionID);

    return new AcceptedResponse();
  }

  /** Создаем новый идентификатор сессии */
  private String generateSessionID() {
    return UUID.randomUUID().toString();
  }

  /** Наполняем базу данных тестовыми данными */
  public void genMockData() throws RequestFailedResponse {
    ConnectToContestRequest c = new ConnectToContestRequest();
    c.login = rootLogin;
    c.password = rootPassword;
    ConnectToContestResponse r = doRequest(c);
    Controller.sessionID = r.sessionID;
    doRequest(new CreateContestRequest("Contest #1"));
    doRequest(new CreateContestRequest("Contest #2"));
    doRequest(new CreateContestRequest("Contest #3"));
  }

  private MockSessionServer getSession(String sessionID)
      throws RequestFailedResponse {
    MockSessionServer sessionData = sessions.get(sessionID);
    if (sessionData == null)
      throw new RequestFailedResponse("Неверный sessionID");
    return sessionData;
  }
}
