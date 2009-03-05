package ru.ipo.dces.client;

import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.exceptions.ServerReturnedError;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;

public interface ServerFacade {

  public AcceptedResponse doRequest(AdjustContestRequest r)
      throws ServerReturnedError, GeneralRequestFailureException;

  public AvailableContestsResponse doRequest(AvailableContestsRequest r)
      throws ServerReturnedError, GeneralRequestFailureException;

  public AcceptedResponse doRequest(ChangePasswordRequest r)
      throws ServerReturnedError, GeneralRequestFailureException;

  public ConnectToContestResponse doRequest(ConnectToContestRequest r)
      throws ServerReturnedError, GeneralRequestFailureException;

  public AcceptedResponse doRequest(CreateContestRequest r)
      throws ServerReturnedError, GeneralRequestFailureException;  

  public AcceptedResponse doRequest(DisconnectRequest r)
      throws ServerReturnedError, GeneralRequestFailureException;

  public GetContestDataResponse doRequest(GetContestDataRequest r)
      throws ServerReturnedError, GeneralRequestFailureException;

  public GetUsersResponse doRequest(GetUsersRequest r)
      throws ServerReturnedError, GeneralRequestFailureException;

  public InstallClientPluginResponse doRequest(InstallClientPluginRequest r)
      throws ServerReturnedError, GeneralRequestFailureException;

  public AcceptedResponse doRequest(RegisterToContestRequest r)
      throws ServerReturnedError, GeneralRequestFailureException;

  public AcceptedResponse doRequest(RemoveClientPluginRequest r)
      throws ServerReturnedError, GeneralRequestFailureException;

  public AcceptedResponse doRequest(RestorePasswordRequest r)
      throws ServerReturnedError, GeneralRequestFailureException;

  public SubmitSolutionResponse doRequest(SubmitSolutionRequest r)
      throws ServerReturnedError, GeneralRequestFailureException;

  public AcceptedResponse doRequest(UploadClientPluginRequest r)
      throws ServerReturnedError, GeneralRequestFailureException;

  public AcceptedResponse doRequest(CreateDataBaseRequest r)
      throws ServerReturnedError, GeneralRequestFailureException;

  public AcceptedResponse doRequest(RemoveUserRequest r)
      throws ServerReturnedError, GeneralRequestFailureException;

  public AcceptedResponse doRequest(RemoveContestRequest r)
      throws ServerReturnedError, GeneralRequestFailureException;

  public AcceptedResponse doRequest(AdjustClientPluginRequest r)
      throws ServerReturnedError, GeneralRequestFailureException;
}
