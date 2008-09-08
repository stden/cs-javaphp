package ru.ipo.dces.tests;

import ru.ipo.dces.clientservercommunication.ContestDescription;

public interface IServer {

  void addContest(String contestName) throws Exception;

  ContestDescription[] getAvaibleContests() throws Exception;

  ContestDescription getContest(int i);

}
