package ru.ipo.dces.clientservercommunication;

import ru.ipo.dces.clientservercommunication.ContestDescription;

/**
 * ќтвет: информаци€ о соревновании, данные содержатс€ в массивах, одна запись дл€
 * каждой задачи
 */
public class GetContestDataResponse implements Response {
  /**
   * ƒанные по запрошенным задачам
   */
  public ProblemDescription problems[];

  /**
   * ќписание соревновани€ 
   */
  public ContestDescription contest;
}
