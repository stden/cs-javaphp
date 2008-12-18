package ru.ipo.dces.client;

import ru.ipo.dces.clientservercommunication.*;

public interface ServerFacade {

  public AcceptedResponse doRequest(AdjustContestRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer;

  public AvailableContestsResponse doRequest(AvailableContestsRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer;

  public AcceptedResponse doRequest(ChangePasswordRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer;

  public ConnectToContestResponse doRequest(ConnectToContestRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer;

  public AcceptedResponse doRequest(CreateContestRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer;

  public AcceptedResponse doRequest(CreateUserRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer;

  public AcceptedResponse doRequest(DisconnectRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer;

  public GetContestDataResponse doRequest(GetContestDataRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer;

  public GetUsersResponse doRequest(GetUsersRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer;

  public InstallClientPluginResponse doRequest(InstallClientPluginRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer;

  public AcceptedResponse doRequest(RegisterToContestRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer;

  public AcceptedResponse doRequest(RemoveClientPluginRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer;

  public AcceptedResponse doRequest(RestorePasswordRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer;

  public SubmitSolutionResponse doRequest(SubmitSolutionRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer;

  public AcceptedResponse doRequest(UploadClientPluginRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer;

  public AcceptedResponse doRequest(CreateDataBaseRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer;

  public AcceptedResponse doRequest(RemoveUserRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer;

  public AcceptedResponse doRequest(RemoveContestRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer;

  public AcceptedResponse doRequest(AdjustClientPluginRequest r)
      throws ServerReturnedError, ServerReturnedNoAnswer;
}
