package ru.ipo.dces.client;

import java.util.*;

import ru.ipo.dces.clientservercommunication.*;

public class MockServer implements IServer {

  private final List<ContestDescription> contestsList = new ArrayList<ContestDescription>();
  private final List<UserDescription>    usersList    = new ArrayList<UserDescription>();

  @Override
  public void addContest(String contestName) {
    ContestDescription contest = new ContestDescription(contestName);
    contest.name = contestName;
    contestsList.add(contest);
  }

  public void addUser(String login, String password) {
    UserDescription ud = new UserDescription();
    ud.login = login;
    ud.password = password;
    usersList.add(ud);
  }

  @Override
  public <T> T doRequest(Class<T> cls, Object obj) throws Exception,
      RequestFailedResponse {
    if (obj instanceof AvailableContestsRequest) {
      AvailableContestsResponse res = new AvailableContestsResponse();
      res.contests = contestsList.toArray(new ContestDescription[0]);
      return cls.cast(res);
    }
    if (obj instanceof ConnectToContestRequest) {
      ConnectToContestRequest cc = (ConnectToContestRequest) obj;
      boolean found = false;
      for (UserDescription user : usersList)
        if (user.login == cc.login && user.password == cc.password) {
          found = true;
          break;
        }
      if (!found) {
        RequestFailedResponse f = new RequestFailedResponse();
        f.message = "Неверный логин или пароль";
        throw f;
      }
      ConnectToContestResponse res = new ConnectToContestResponse();
      res.sessionID = "sdfgdsgdf";
      return cls.cast(res);
    }
    return null;
  }

  @Override
  public ContestDescription[] getAvaibleContests() {
    return contestsList.toArray(new ContestDescription[0]);
  }

  @Override
  public ContestDescription getContest(int i) {
    return contestsList.get(i);
  }

}
