package ru.ipo.dces.clientservercommunication;

/**
 * Ответ: Решение отправлено
 */
public class SubmitSolutionResponse implements Response {
  /** вернувшиеся данные о решенной задаче */
  String problemResult;
  // возможно, дополнительные данные о процессе проверки
}
