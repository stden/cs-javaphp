package ru.ipo.dces.server.http;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.net.*;
import java.util.Arrays;

import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.exceptions.ServerReturnedError;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;
import ru.ipo.dces.log.LoggerFactory;
import ru.ipo.dces.log.LogMessageType;
import ru.ipo.dces.server.ServerFacade;

public class HttpServer implements ServerFacade {

    private final String              URL_string;
    private final byte[] REQUEST_VAR = "x=".getBytes(PHP.SERVER_CHARSET);    

    public HttpServer(String URL_string) {
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

    public AdjustContestResponse doRequest(AdjustContestRequest r)
            throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(AdjustContestResponse.class, r);
    }

    public AvailableContestsResponse doRequest(AvailableContestsRequest r)
            throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(AvailableContestsResponse.class, r);
    }

    public <T extends Response> T doRequest(Class<T> cls, Request obj)
            throws ServerReturnedError, GeneralRequestFailureException {

        InputStream input;
        RequestFailedResponse failedResponse;

        try {
            input = doPost(obj);
            input = new BufferedInputStream(input, 4096);
            input.mark(4096);          
        } catch (Exception e) {
            LoggerFactory.getLogger().log("Не удалось соединиться с сервером (ошибка в system.err)", LogMessageType.Error, null);
            e.printStackTrace();
            throw new GeneralRequestFailureException();
        }

        try {
          failedResponse = PHP.unserialize(RequestFailedResponse.class, input);
          if (failedResponse == null)
            return PHP.unserialize(cls, input);
        } catch (Exception e) {
          LoggerFactory.getLogger().log("Произошла ошибка связи с сервером", LogMessageType.Error, null);
          try {
            byte[] actualAnswer;
            input.reset();
            actualAnswer = inputStreamToByteArray(0, input);
            System.err.println("actual server answer: " + new String(actualAnswer, PHP.SERVER_CHARSET));
          } catch (IOException ioe) {
            System.err.println("failed to get actual server answer");
          }

          throw new GeneralRequestFailureException();
        }

        //now failedResponse != null
        switch (failedResponse.failReason) {
          case BrokenServerError:
            LoggerFactory.getLogger().log(
                    "Ошибка на стороне сервера №" + failedResponse.failErrNo +
                            (failedResponse.extendedInfo == null ? "" : ". " + failedResponse.extendedInfo),
                    LogMessageType.Error,
                    null
            );
            throw new GeneralRequestFailureException();
          case BrokenServerPluginError:
            LoggerFactory.getLogger().log(
                    "Ошибка на стороне сервера №" + failedResponse.failErrNo +
                            (failedResponse.extendedInfo == null ? "" : ". " + failedResponse.extendedInfo),
                    LogMessageType.Error,
                    null
            );
            throw new GeneralRequestFailureException();
          case BusinessLogicError:
            throw new ServerReturnedError(failedResponse.failErrNo,  failedResponse.extendedInfo);
        }

      LoggerFactory.getLogger().log("Неизвестная ошибка при попытке связи с сервером", LogMessageType.Error, null);
      throw new GeneralRequestFailureException();
    }

    private byte[] inputStreamToByteArray(int fromIndex, InputStream input) throws IOException {
        byte[] buf = new byte[4096];
        int n;
        for (int i = fromIndex; (n = input.read()) != -1 ; i++ )
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

    public ConnectToContestResponse doRequest(ConnectToContestRequest r)
            throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(ConnectToContestResponse.class, r);
    }

    public CreateContestResponse doRequest(CreateContestRequest r)
            throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(CreateContestResponse.class, r);
    }

    public AcceptedResponse doRequest(DisconnectRequest r)
            throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(AcceptedResponse.class, r);
    }

    public GetContestDataResponse doRequest(GetContestDataRequest r)
            throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(GetContestDataResponse.class, r);
    }

    public GetUsersResponse doRequest(GetUsersRequest r)
            throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(GetUsersResponse.class, r);
    }

    public DownloadPluginResponse doRequest(DownloadPluginRequest r)
            throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(DownloadPluginResponse.class, r);
    }

    public AcceptedResponse doRequest(RegisterToContestRequest r)
            throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(AcceptedResponse.class, r);
    }

    public AcceptedResponse doRequest(RemoveClientPluginRequest r)
            throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(AcceptedResponse.class, r);
    }

    public AcceptedResponse doRequest(RestorePasswordRequest r)
            throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(AcceptedResponse.class, r);
    }

    public SubmitSolutionResponse doRequest(SubmitSolutionRequest r)
            throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(SubmitSolutionResponse.class, r);
    }

    public AcceptedResponse doRequest(CreateDataBaseRequest r) throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(AcceptedResponse.class, r);
    }

    public AcceptedResponse doRequest(RemoveUserRequest r) throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(AcceptedResponse.class, r);
    }

    public AcceptedResponse doRequest(RemoveContestRequest r) throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(AcceptedResponse.class, r);
    }

    public AcceptedResponse doRequest(AdjustPluginRequest r) throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(AcceptedResponse.class, r);
    }

    public GetContestResultsResponse doRequest(GetContestResultsRequest r) throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(GetContestResultsResponse.class,  r);
    }

    public AcceptedResponse doRequest(StopContestRequest r) throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(AcceptedResponse.class, r);
    }

    public AvailablePluginsResponse doRequest(AvailablePluginsRequest r) throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(AvailablePluginsResponse.class, r); 
    }

}
