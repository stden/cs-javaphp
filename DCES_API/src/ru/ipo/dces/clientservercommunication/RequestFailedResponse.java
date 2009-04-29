package ru.ipo.dces.clientservercommunication;

/**
 * Ответ: запрос окончился неудачей
 */
public class RequestFailedResponse implements Response {

  public enum FailReason {
    BusinessLogicError,
    BrokenServerError,
    BrokenServerPluginError,
  }

  public FailReason failReason;

  /**
   * Номер ошибки, см. таблица
   */
  public int        failErrNo;

  /**
   * Дополнительная к ошибке информация
   * Для BuisnessLogicError - что угодно
   * Для BrokenServerError  - скорее всего, описание проблемы с MySQL
   * Для BrokenServerPluginError - описание плагина
   */
  public String     extendedInfo;

}