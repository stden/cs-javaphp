package ru.ipo.dces.client;

import ru.ipo.dces.clientservercommunication.*;

public interface ServerFacade {

  public AcceptedResponse doRequest(AdjustContestRequest r) throws Exception;

  public AvailableContestsResponse doRequest(AvailableContestsRequest r)
      throws Exception;

  public AcceptedResponse doRequest(ChangePasswordRequest r) throws Exception;

  public ConnectToContestResponse doRequest(ConnectToContestRequest r)
      throws Exception;

  public AcceptedResponse doRequest(CreateContestRequest r) throws Exception;

  public AcceptedResponse doRequest(CreateUserRequest r) throws Exception;

  public AcceptedResponse doRequest(DisconnectRequest r) throws Exception;

  public GetContestDataResponse doRequest(GetContestDataRequest r)
      throws Exception;

  public GetUsersResponse doRequest(GetUsersRequest r) throws Exception;

  public InstallClientPluginResponse doRequest(InstallClientPluginRequest r)
      throws Exception;

  public AcceptedResponse doRequest(RegisterToContestRequest r)
      throws Exception;

  public AcceptedResponse doRequest(RemoveClientPluginRequest r)
      throws Exception;

  public AcceptedResponse doRequest(
      RestorePasswordRequest restorePasswordRequest) throws Exception;

  public AcceptedResponse doRequest(
      UploadClientPluginRequest uploadClientPluginRequest) throws Exception;
}
