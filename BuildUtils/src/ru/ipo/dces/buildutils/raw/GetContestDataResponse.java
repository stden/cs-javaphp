package ru.ipo.dces.buildutils.raw;

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
