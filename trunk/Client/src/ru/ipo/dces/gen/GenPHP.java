package ru.ipo.dces.gen;

import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.tests.samples.Sum;

public class GenPHP {

  static Class<?> messages[] = { AcceptedResponse.class,
      AdjustContestRequest.class, AvailableContestsRequest.class,
      AvailableContestsResponse.class, ChangePasswordRequest.class,
      ConnectToContestRequest.class, ConnectToContestResponse.class,
      ContestDescription.class, CreateContestRequest.class,
      CreateUserRequest.class, DisconnectRequest.class,
      GetContestDataRequest.class, GetContestDataResponse.class,
      GetUsersRequest.class, GetUsersResponse.class,
      InstallClientPluginRequest.class, InstallClientPluginResponse.class,
      ProblemDescription.class, RegisterToContestRequest.class,
      RemoveClientPluginRequest.class, RemoveContestRequest.class,
      RemoveUserRequest.class, RequestFailedResponse.class,
      RestorePasswordRequest.class, SubmitSolutionRequest.class,
      SubmitSolutionResponse.class, UploadClientPluginRequest.class,
      UserDescription.class, Sum.class };

  /**
   * @param args
   */
  public static void main(String[] args) {
    for (Class<?> cls : messages)
      System.out.println("class " + cls.getSimpleName() + "{};");
  }

}
