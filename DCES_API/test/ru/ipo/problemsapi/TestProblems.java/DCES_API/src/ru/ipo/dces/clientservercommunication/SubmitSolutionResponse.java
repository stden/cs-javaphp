package ru.ipo.dces.clientservercommunication;

import java.util.HashMap;

/**
 * ќтвет: –ешение отправлено
 */
public class SubmitSolutionResponse implements Response {
  /** вернувшиес€ данные о решенной задаче.
   * ¬ид ответа может быть произвольный.  аждый плагин клииента знает, какой ответ он ожидает от системы
   * (от плагина стороны сервера)
   */
  public HashMap<String, String> problemResult;
}
