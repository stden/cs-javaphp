package ru.ipo.dces.client;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;

import ru.ipo.dces.clientservercommunication.*;

public class RealServer implements ServerFacade {

  private final String              URL_string; 
  private final byte[] REQUEST_VAR = "x=".getBytes(PHP.SERVER_CHARSET);

  public RealServer(String URL_string) {
    this.URL_string = URL_string;
  }

  public InputStream doPost(Object sendToServer) throws Exception {
    HttpURLConnection con = (HttpURLConnection) new URL(URL_string)
        .openConnection();
    con.setDoInput(true);
    con.setUseCaches(false);

    con.setRequestMethod("POST");
    con.setDoOutput(true);    

    final OutputStream out = con.getOutputStream();
    //print request
    out.write(REQUEST_VAR);
    PHP.serialize(sendToServer, out);
    out.close();

    // Read answer
    InputStream in = con.getInputStream();
    if (con.getResponseCode() != HttpURLConnection.HTTP_OK)
      throw new ConnectException("Соединение с " + URL_string + " не удалось!");

    return in;
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
    InputStream input;

    final byte[] buf;
    try {
      input = doPost(obj);
      buf = inputStreamToByteArray(input);
    } catch (Exception e) {
      throw new ServerReturnedNoAnswer("Ошибка соединения с сервером");
    }
    try {
      return PHP.unserialize(cls, new ByteArrayInputStream(buf));
    } catch (IllegalClassException e) {
      
      RequestFailedResponse response;
      try {
          response = PHP.unserialize(RequestFailedResponse.class, new ByteArrayInputStream(buf));
      } catch (Exception e1) {
          throw new ServerReturnedNoAnswer("Неправильный формат ответа сервера", new String(buf, PHP.SERVER_CHARSET));
      }
      throw new ServerReturnedError(response.message);

    } catch (Exception e) {
      e.printStackTrace();
      throw new ServerReturnedNoAnswer("Неправильный формат ответа сервера", new String(buf, PHP.SERVER_CHARSET));
    }
  }

  private byte[] inputStreamToByteArray(InputStream input) throws IOException {
    byte[] buf = new byte[4096];
    int n;
    for (int i = 0; (n = input.read()) != -1 ; i++ )
    {
      if (i >= buf.length) {
        //copy array
        buf = Arrays.copyOf(buf, buf.length << 1);
        if (buf.length > 4 * 1024 * 1024) throw new IOException("Too long input stream");
      }
      buf[i] = (byte)n;
    }
    return buf;
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

  public AcceptedResponse doRequest(CreateDataBaseRequest r) throws ServerReturnedError, ServerReturnedNoAnswer {
    return doRequest(AcceptedResponse.class, r);
  } 
}
