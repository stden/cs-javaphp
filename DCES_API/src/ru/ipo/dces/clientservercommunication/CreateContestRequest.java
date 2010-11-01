package ru.ipo.dces.clientservercommunication;

/**
 * Запрос на создание нового соревнования
 */
public class CreateContestRequest implements Request {

  /**
   * Идентификатор сессии. Подходит сессия только администратора сервера
   */
  @PHPDefaultValue("null")
  public String             sessionID;

  /**
   * Описание создаваемого соревнования. Значение contest.contestID игнорируется, остальные поля обязательно
   * должны быть заполнены, т.е. не допускается использовать null в качестве значения
   */

  @PHPDefaultValue("")
  public ContestDescription contest;

}
