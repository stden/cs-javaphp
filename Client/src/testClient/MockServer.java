package testClient;

import java.util.*;

import Client.Contest;

public class MockServer implements IServer {

  private final List<Contest> contestsList = new ArrayList<Contest>();

  @Override
  public void addContest(String contestName) {
    Contest contest = new Contest(contestName);
    contestsList.add(contest);
  }

  @Override
  public List<Contest> getAvaibleContests() {
    return contestsList;
  }

  @Override
  public Contest getContest(int i) {
    return contestsList.get(i);
  }

}
