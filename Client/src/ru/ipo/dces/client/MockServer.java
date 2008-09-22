package ru.ipo.dces.client;

import java.util.*;

import ru.ipo.dces.clientservercommunication.*;

public class MockServer implements IServer {

  private final List<ContestDescription> contestsList = new ArrayList<ContestDescription>();
  private final List<UserDescription>    usersList    = new ArrayList<UserDescription>();

  @Override
  public <T> T doRequest(Class<T> cls, Request obj) throws Exception,
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
    if (obj instanceof ChangePasswordRequest)
      return cls.cast(new AcceptedResponse());
    if (obj instanceof CreateContestRequest) {
      CreateContestRequest ccr = (CreateContestRequest) obj;
      contestsList.add(ccr.contest);
      return cls.cast(new AcceptedResponse());
    }
    if (obj instanceof CreateUserRequest) {
      CreateUserRequest cur = (CreateUserRequest) obj;
      usersList.add(cur.user);
      return cls.cast(new AcceptedResponse());
    }
    return null;
  }
}
