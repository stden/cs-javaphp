package ru.ipo.dces.client;

import java.io.*;
import java.net.*;
import java.util.HashMap;

import ru.ipo.dces.clientservercommunication.*;

public class RealServer implements ServerFacade {

  private final String              URL_string; 
  private final String REQUEST_VAR = "x=";

  static HashMap<Character, String> rep = new HashMap<Character, String>();

  static {
    rep.put('%', "%25");
    rep.put('&', "%26");
    rep.put((char) 0x00, "%00");
    rep.put('+', "%2B");
    rep.put((char) 0x0A, "%0A");
    rep.put((char) 0x0D, "%0D");
  }

  public RealServer(String URL_string) {
    this.URL_string = URL_string;
  }

  public String doPost(Object sendToServer) throws Exception {
    HttpURLConnection con = (HttpURLConnection) new URL(URL_string)
        .openConnection();
    con.setDoInput(true);
    con.setUseCaches(false);

    con.setRequestMethod("POST");
    con.setDoOutput(true);

    OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), PHP.SERVER_CHARSET);
    PrintWriter out = new PrintWriter(osw, true);

    out.print(REQUEST_VAR);
    PHP.serialize(sendToServer, out);
    out.close();

    // Read answer
    BufferedReader in = new BufferedReader(new InputStreamReader(con
        .getInputStream(), PHP.SERVER_CHARSET));
    if (con.getResponseCode() != HttpURLConnection.HTTP_OK)
      throw new ConnectException("Соединение с " + URL_string + " не удалось!");
    StringBuffer pageContents = new StringBuffer();
    String curLine = in.readLine();
    while (curLine != null) {
      pageContents.append(curLine);
      curLine = in.readLine();
    }

    //if Unicode: remove the first symbol (BOM byte order marker)
    do {
      if (pageContents.length() > 0) {
        char firstChar = pageContents.charAt(0);
        if (firstChar == '\uFEFF' || firstChar == '\uFFFE')
          pageContents.delete(0, 1);
        else
          break;
      }
      else break;      
    } while (true);

    return pageContents.toString();
  }

  @Override
  public AcceptedResponse doRequest(AdjustContestRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer {
    return doRequest(AcceptedResponse.class, r);
  }

  @Override
  public AvailableContestsResponse doRequest(AvailableContestsRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer {
    return doRequest(AvailableContestsResponse.class, r);
  }

  @Override
  public AcceptedResponse doRequest(ChangePasswordRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer {
    return doRequest(AcceptedResponse.class, r);
  }

  public <T> T doRequest(Class<T> cls, Request obj)
      throws ServerReturnedError, ServerReturnedNoAnswer {
    String answer;
    try {
      answer = doPost(obj);
    } catch (Exception e) {
      throw new ServerReturnedNoAnswer("Ошибка соединения с сервером");
    }
    try {
      return PHP.unserialize(cls, answer);
    } catch (IllegalClassException e) {
      try {
          RequestFailedResponse response = PHP.unserialize(RequestFailedResponse.class, answer);
          throw new ServerReturnedError(response.message);
      } catch (Exception e1) {
          throw new ServerReturnedNoAnswer("Неправильный формат ответа сервера");
      }
    } catch (Exception e) {
      throw new ServerReturnedNoAnswer("Неправильный формат ответа сервера");
    }
  }

  @Override
  public ConnectToContestResponse doRequest(ConnectToContestRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer {
    return doRequest(ConnectToContestResponse.class, r);
  }

  @Override
  public AcceptedResponse doRequest(CreateContestRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer {
    return doRequest(AcceptedResponse.class, r);
  }

  @Override
  public AcceptedResponse doRequest(CreateUserRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer {
    return doRequest(AcceptedResponse.class, r);
  }

  @Override
  public AcceptedResponse doRequest(DisconnectRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer {
    return doRequest(AcceptedResponse.class, r);
  }

  @Override
  public GetContestDataResponse doRequest(GetContestDataRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer {
    return doRequest(GetContestDataResponse.class, r);
  }

  @Override
  public GetUsersResponse doRequest(GetUsersRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer {
    return doRequest(GetUsersResponse.class, r);
  }

  @Override
  public InstallClientPluginResponse doRequest(InstallClientPluginRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer {
    return doRequest(InstallClientPluginResponse.class, r);
  }

  @Override
  public AcceptedResponse doRequest(RegisterToContestRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer {
    return doRequest(AcceptedResponse.class, r);
  }

  @Override
  public AcceptedResponse doRequest(RemoveClientPluginRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer {
    return doRequest(AcceptedResponse.class, r);
  }

  @Override
  public AcceptedResponse doRequest(RestorePasswordRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer {
    return doRequest(AcceptedResponse.class, r);
  }

  @Override
  public SubmitSolutionResponse doRequest(SubmitSolutionRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer {
    return doRequest(SubmitSolutionResponse.class, r);
  }

  @Override
  public AcceptedResponse doRequest(UploadClientPluginRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer {
    return doRequest(AcceptedResponse.class, r);
  }

  // Подготовка строки к передаче в GET/POST запросе
  public String StringPrepare(String string) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < string.length(); i++)
      if (rep.containsKey(string.charAt(i)))
        sb.append(rep.get(string.charAt(i)));
      else
        sb.append(string.charAt(i));
    return sb.toString();
  }
}
