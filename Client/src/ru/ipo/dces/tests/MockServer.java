package ru.ipo.dces.tests;

import java.util.*;

import ru.ipo.dces.clientservercommunication.ContestDescription;

public class MockServer implements IServer {

  private final List<ContestDescription> contestsList = new ArrayList<ContestDescription>();

  @Override
  public void addContest(String contestName) {
    ContestDescription contest = new ContestDescription();
    contest.name = contestName;
    contestsList.add(contest);
  }

  @Override
  public ContestDescription[] getAvaibleContests() {
    return contestsList.toArray(new ContestDescription[0]);
  }

  @Override
  public ContestDescription getContest(int i) {
    return contestsList.get(i);
  }

}
