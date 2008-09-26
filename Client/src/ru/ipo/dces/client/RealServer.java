package ru.ipo.dces.client;

import java.io.*;
import java.net.*;
import java.util.HashMap;

import ru.ipo.dces.clientservercommunication.*;

public class RealServer implements IServer {

  private final String              URL_string;

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

  public String doPost(String sendToServer) throws MalformedURLException,
      IOException {
    HttpURLConnection con = (HttpURLConnection) new URL(URL_string)
        .openConnection();
    con.setDoInput(true);
    con.setUseCaches(false);

    con.setRequestMethod("POST");
    con.setDoOutput(true);

    PrintWriter out = new PrintWriter(con.getOutputStream(), true);
    out.println(sendToServer);

    // Read answer
    BufferedReader in = new BufferedReader(new InputStreamReader(con
        .getInputStream()));
    if (con.getResponseCode() != HttpURLConnection.HTTP_OK)
      throw new ConnectException("Соединение с " + URL_string + " не удалось!");
    StringBuffer pageContents = new StringBuffer();
    String curLine = in.readLine();
    while (curLine != null) {
      pageContents.append(curLine);
      curLine = in.readLine();
    }
    String result = pageContents.toString();
    return result;
  }

  @Override
  public AcceptedResponse doRequest(AdjustContestRequest r) throws Exception {
    return doRequest(AcceptedResponse.class, r);
  }

  @Override
  public AvailableContestsResponse doRequest(AvailableContestsRequest r)
      throws Exception {
    return doRequest(AvailableContestsResponse.class, r);
  }

  @Override
  public AcceptedResponse doRequest(ChangePasswordRequest r) throws Exception {
    return doRequest(AcceptedResponse.class, r);
  }

  public <T> T doRequest(Class<T> cls, Request obj) throws Exception {
    String answer = doPost("x=" + PHP.serialize(obj));
    try {
      return PHP.unserialize(cls, answer);
    } catch (Exception e2) {
      try {
        throw PHP.unserialize(RequestFailedResponse.class, answer);
      } catch (Exception e4) {
        throw new RequestFailedResponse(answer);
      }
    }
  }

  @Override
  public ConnectToContestResponse doRequest(ConnectToContestRequest r)
      throws Exception {
    return doRequest(ConnectToContestResponse.class, r);
  }

  @Override
  public AcceptedResponse doRequest(CreateContestRequest r) throws Exception {
    return doRequest(AcceptedResponse.class, r);
  }

  @Override
  public AcceptedResponse doRequest(CreateUserRequest r) throws Exception {
    return doRequest(AcceptedResponse.class, r);
  }

  @Override
  public AcceptedResponse doRequest(DisconnectRequest r) throws Exception {
    return doRequest(AcceptedResponse.class, r);
  }

  @Override
  public GetContestDataResponse doRequest(GetContestDataRequest r)
      throws Exception {
    return doRequest(GetContestDataResponse.class, r);
  }

  @Override
  public GetUsersResponse doRequest(GetUsersRequest r) throws Exception {
    return doRequest(GetUsersResponse.class, r);
  }

  @Override
  public InstallClientPluginResponse doRequest(InstallClientPluginRequest r)
      throws Exception {
    return doRequest(InstallClientPluginResponse.class, r);
  }

  @Override
  public AcceptedResponse doRequest(RegisterToContestRequest r)
      throws Exception {
    return doRequest(AcceptedResponse.class, r);
  }

  @Override
  public AcceptedResponse doRequest(RemoveClientPluginRequest r)
      throws Exception {
    return doRequest(AcceptedResponse.class, r);
  }

  @Override
  public AcceptedResponse doRequest(RestorePasswordRequest r) throws Exception {
    return doRequest(AcceptedResponse.class, r);
  }

  @Override
  public AcceptedResponse doRequest(UploadClientPluginRequest r)
      throws Exception {
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
