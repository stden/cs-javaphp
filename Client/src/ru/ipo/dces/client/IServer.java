package ru.ipo.dces.client;

import ru.ipo.dces.clientservercommunication.*;

public interface IServer {

  void addContest(String contestName) throws Exception;

  public <T> T doRequest(Class<T> cls, Object obj) throws Exception,
      RequestFailedResponse;

  ContestDescription[] getAvaibleContests() throws Exception,
      RequestFailedResponse;

  ContestDescription getContest(int i);

}
