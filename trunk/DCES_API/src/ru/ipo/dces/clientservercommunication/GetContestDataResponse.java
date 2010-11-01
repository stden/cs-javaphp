package ru.ipo.dces.clientservercommunication;

import ru.ipo.dces.clientservercommunication.ContestDescription;

/**
 * Ответ: информация о соревновании, данные содержатся в массивах, одна запись для
 * каждой задачи
 */
public class GetContestDataResponse implements Response {
  /**
   * Данные по запрошенным задачам
   */
  public ProblemDescription problems[];

  /**
   * Описание соревнования 
   */
  public ContestDescription contest;
}
