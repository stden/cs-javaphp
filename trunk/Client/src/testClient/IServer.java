package testClient;

import java.util.List;

import Client.Contest;

public interface IServer {

  void addContest(String contestName);

  List<Contest> getAvaibleContests();

  Contest getContest(int i);

}
