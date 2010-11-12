package ru.ipo.dces.buildutils.raw;

import java.util.HashMap;

/**
 * Ответ: Решение отправлено
 */
public class SubmitSolutionResponse implements Response {
  /** вернувшиеся данные о решенной задаче.
   * Вид ответа может быть произвольный. Каждый плагин клииента знает, какой ответ он ожидает от системы
   * (от плагина стороны сервера)
   */
  public HashMap<String, String> problemResult;
}
