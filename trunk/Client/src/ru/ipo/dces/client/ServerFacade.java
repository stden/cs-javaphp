package ru.ipo.dces.client;

import ru.ipo.dces.clientservercommunication.*;

public interface ServerFacade {

  public AcceptedResponse doRequest(AdjustContestRequest r)
      throws RequestFailedResponse;

  public AvailableContestsResponse doRequest(AvailableContestsRequest r)
      throws RequestFailedResponse;

  public AcceptedResponse doRequest(ChangePasswordRequest r)
      throws RequestFailedResponse;

  public ConnectToContestResponse doRequest(ConnectToContestRequest r)
      throws RequestFailedResponse;

  public AcceptedResponse doRequest(CreateContestRequest r)
      throws RequestFailedResponse;

  public AcceptedResponse doRequest(CreateUserRequest r)
      throws RequestFailedResponse;

  public AcceptedResponse doRequest(DisconnectRequest r)
      throws RequestFailedResponse;

  public GetContestDataResponse doRequest(GetContestDataRequest r)
      throws RequestFailedResponse;

  public GetUsersResponse doRequest(GetUsersRequest r)
      throws RequestFailedResponse;

  public InstallClientPluginResponse doRequest(InstallClientPluginRequest r)
      throws RequestFailedResponse;

  public AcceptedResponse doRequest(RegisterToContestRequest r)
      throws RequestFailedResponse;

  public AcceptedResponse doRequest(RemoveClientPluginRequest r)
      throws RequestFailedResponse;

  public AcceptedResponse doRequest(RestorePasswordRequest r)
      throws RequestFailedResponse;

  public SubmitSolutionResponse doRequest(SubmitSolutionRequest r)
      throws RequestFailedResponse;

  public AcceptedResponse doRequest(UploadClientPluginRequest r)
      throws RequestFailedResponse;
}
