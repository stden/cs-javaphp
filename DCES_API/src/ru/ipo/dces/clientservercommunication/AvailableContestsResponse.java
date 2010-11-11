package ru.ipo.dces.clientservercommunication;

/**
 * Ответ на запрос о списке соревнований AvailableContestsRequest.
 */
public class AvailableContestsResponse implements Response {

  /**
   * Массив с описаниями всех зарегистрированных в системе соревнованиях
   */
  @BinInfo(phpDefaultValue="array()")
  public ContestDescription[] contests;

}
