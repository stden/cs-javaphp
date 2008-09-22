package ru.ipo.dces.clientservercommunication;

/**
 * Ответ: информация о контесте, данные содержатся в массивах, одна запись для
 * каждой задачи
 */
public class GetContestDataResponse implements Response {
  public ProblemDescription problems[];
  public ContestDescription contest;
}
