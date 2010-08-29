package ru.ipo.dces.gen;

import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.clientservercommunication.ContestDescription;

public class GenPHP {

  static Class<?> messages[] = { AcceptedResponse.class,
      AdjustContestRequest.class, AvailableContestsRequest.class,
      AvailableContestsResponse.class,
      ConnectToContestRequest.class, ConnectToContestResponse.class,
      ContestDescription.class, CreateContestRequest.class,
      DisconnectRequest.class,
      GetContestDataRequest.class, GetContestDataResponse.class,
      GetUsersRequest.class, GetUsersResponse.class,
      DownloadPluginRequest.class, DownloadPluginResponse.class,
      ProblemDescription.class, RegisterToContestRequest.class,
      RemovePluginRequest.class, RemoveContestRequest.class,
      RemoveUserRequest.class, RequestFailedResponse.class,
      RestorePasswordRequest.class, SubmitSolutionRequest.class,
      SubmitSolutionResponse.class,
      UserDescription.class/*, Sum.class*/ };

  /**
   * @param args
   */
  public static void main(String[] args) {
    for (Class<?> cls : messages)
      System.out.println("class " + cls.getSimpleName() + "{};");
  }

}
