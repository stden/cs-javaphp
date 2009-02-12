package ru.ipo.dces.client;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;

import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.exceptions.ServerReturnedError;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;

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
            throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(AcceptedResponse.class, r);
    }

    @Override
    public AvailableContestsResponse doRequest(AvailableContestsRequest r)
            throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(AvailableContestsResponse.class, r);
    }

    @Override
    public AcceptedResponse doRequest(ChangePasswordRequest r)
            throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(AcceptedResponse.class, r);
    }

    public <T> T doRequest(Class<T> cls, Request obj)
            throws ServerReturnedError, GeneralRequestFailureException {

        InputStream input;
        RequestFailedResponse failedResponse = null;
        int[] magic;
        byte[] bytes = null;

        try {
            input = doPost(obj);
        } catch (Exception e) {
            Controller.getLogger().log("Ошибка соединения с сервером", UserMessagesLogger.LogMessageType.Error, this);
            throw new GeneralRequestFailureException();
        }

        try {
            //test magic
            magic = new int[4];
            magic[0] = input.read();
            magic[1] = input.read();
            magic[2] = input.read();
            magic[3] = input.read();
            if (magic[0] != 4 || magic[1] != 2 || magic[2] != 3 || magic[3] != 9) {
                bytes = inputStreamToByteArray(4, input);
                for (int i = 0; i < 4; i++)
                    if (magic[i] != -1) bytes[i] = (byte)magic[i]; else break;
            } else {
                failedResponse = PHP.unserialize(RequestFailedResponse.class, input);
                if (failedResponse == null)
                    return PHP.unserialize(cls, input);
            }
        } catch (Exception e) {
            Controller.getLogger().log("Произошла ошибка связи с сервером", UserMessagesLogger.LogMessageType.Error, this);
            //throw new CommunicationFailedException(); //TODO: refactor PHP server to process BrokenServer exceptions
            throw new GeneralRequestFailureException();
        }

        if (failedResponse != null)
            throw new ServerReturnedError(failedResponse.message);
        if (bytes != null) {
            System.err.println("Неправильный формат ответа сервера: " + new String(bytes, PHP.SERVER_CHARSET));
            Controller.getLogger().log("Произошла ошибка связи с сервером", UserMessagesLogger.LogMessageType.Error, this);
            //throw new CommunicationFailedException(); //TODO: refactor PHP server to process BrokenServer exceptions
            throw new GeneralRequestFailureException();
        }
        System.err.println("Ошибка неизвестного типа");
        Controller.getLogger().log("Произошла ошибка связи с сервером", UserMessagesLogger.LogMessageType.Error, this);
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

    @Override
    public ConnectToContestResponse doRequest(ConnectToContestRequest r)
            throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(ConnectToContestResponse.class, r);
    }

    @Override
    public AcceptedResponse doRequest(CreateContestRequest r)
            throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(AcceptedResponse.class, r);
    }

    @Override
    public AcceptedResponse doRequest(CreateUserRequest r)
            throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(AcceptedResponse.class, r);
    }

    @Override
    public AcceptedResponse doRequest(DisconnectRequest r)
            throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(AcceptedResponse.class, r);
    }

    @Override
    public GetContestDataResponse doRequest(GetContestDataRequest r)
            throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(GetContestDataResponse.class, r);
    }

    @Override
    public GetUsersResponse doRequest(GetUsersRequest r)
            throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(GetUsersResponse.class, r);
    }

    @Override
    public InstallClientPluginResponse doRequest(InstallClientPluginRequest r)
            throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(InstallClientPluginResponse.class, r);
    }

    @Override
    public AcceptedResponse doRequest(RegisterToContestRequest r)
            throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(AcceptedResponse.class, r);
    }

    @Override
    public AcceptedResponse doRequest(RemoveClientPluginRequest r)
            throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(AcceptedResponse.class, r);
    }

    @Override
    public AcceptedResponse doRequest(RestorePasswordRequest r)
            throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(AcceptedResponse.class, r);
    }

    @Override
    public SubmitSolutionResponse doRequest(SubmitSolutionRequest r)
            throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(SubmitSolutionResponse.class, r);
    }

    @Override
    public AcceptedResponse doRequest(UploadClientPluginRequest r)
            throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(AcceptedResponse.class, r);
    }

    public AcceptedResponse doRequest(CreateDataBaseRequest r) throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(AcceptedResponse.class, r);
    }

    @Override
    public AcceptedResponse doRequest(RemoveUserRequest r) throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(AcceptedResponse.class, r);
    }

    public AcceptedResponse doRequest(RemoveContestRequest r) throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(AcceptedResponse.class, r);
    }

    public AcceptedResponse doRequest(AdjustClientPluginRequest r) throws ServerReturnedError, GeneralRequestFailureException {
        return doRequest(AcceptedResponse.class, r);
    }
}
