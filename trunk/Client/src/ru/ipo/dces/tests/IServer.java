package ru.ipo.dces.tests;

import java.util.List;

import ru.ipo.dces.client.Contest;


public interface IServer {

  void addContest(String contestName);

  List<Contest> getAvaibleContests();

  Contest getContest(int i);

}
