package ru.ipo.dces.client;

import ru.ipo.dces.clientservercommunication.*;

public interface IServer {

  public <T> T doRequest(Class<T> cls, Request obj) throws Exception,
      RequestFailedResponse;
}
