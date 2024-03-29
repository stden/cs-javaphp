package ru.ipo.dces.buildutils.raw;

/**
 * Универсальный ответ, может являться ответом на любой запрос. Происходит в случае невозможности обработать запрос
 */
public class RequestFailedResponse implements Response {

  /**
   * Поле описывает причину неудачи
   */
  public enum FailReason {
    /**Неудача произошла, потому что запрос выполнить невозможно. Например, на него не хватает прав, запрос
     * неправильно оформен или запрос желает сделать что-то принципиально невозможное
     */
    BusinessLogicError,
    /**Неудача произошла из-за внутренней ошибки сервера. Требуется обратиться к разработчикам*/
    BrokenServerError,
    /**Неудача произошла из-за внутренней ошибки плагина стороны сервера.
     * Требуется обратиться к разработчикам плагина */
    BrokenServerPluginError,
  }

  /**
   * Причина неудачи
   */
  public FailReason failReason;

  /**
   * Номер ошибки, в случае failReason == BrokenServerError или BrokerServerPluginError номер
   * может разобрать только автор сервера или плагина соотвественно. В случае failReason == BusinessLogicError
   * смысл номера ошибки определяется в соответсвии с таблицей:
   * <ol start="0">
   * <li>Недостаточно прав для совершения операции</li>
   * <li>Указано недостаточно параметров для совершения операции</li>
   * <li>Не удается найти указанного пользователя</li>
   * <li>Неверно указана сессия</li>
   * <li>Не удается найти указанную задачу</li>
   * <li>Не удается найти указанный серверный плагин</li>
   * <li>Не удается найти указанный клиентский плагин</li>
   * <li>Данные с условием не являются zip файлом</li>
   * <li>Данные с ответом не являются zip файлом</li>
   * <li>Серверный плагин не принял данные с условием</li>
   * <li>Серверный плагин не принял данные с ответом</li>
   * <li>Запрос не произвел изменений</li>
   * <li>Контест, логин или пароль указаны неверно</li>
   * <li>База данных уже существует</li>
   * <li>Не удается найти указанный контест</li>
   * <li>Не удается разобрать запрос</li>
   * <li>Нельзя удалять нулевой контест</li>
   * <li>Пользователь с данным логином уже зарегистрирован</li>
   * <li>Суперадминистратор может быть зарегитрирован только для нулевого контеста</li>
   * <li>Соревнование еще не началось</li>
   * <li>Соревнование уже закончилось</li>      
   * </ol>
   */
  public int        failErrNo;

  /**
   * Дополнительная к ошибке информация
   * Для BuisnessLogicError - не используется
   * Для BrokenServerError  - скорее всего, описание проблемы с БД
   * Для BrokenServerPluginError - описание, переданное плагином
   */
  public String     extendedInfo;

}