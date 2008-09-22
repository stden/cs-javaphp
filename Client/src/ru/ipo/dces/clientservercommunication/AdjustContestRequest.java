package ru.ipo.dces.clientservercommunication;

/**
 * Получить задачи текущего контеста
 */
public class AdjustContestRequest implements Request {

  public String               sessionID;
  public ContestDescription   contest;
  public ProblemDescription[] problems;

}
