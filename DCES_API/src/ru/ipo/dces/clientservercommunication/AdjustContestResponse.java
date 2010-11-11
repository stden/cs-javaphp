package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 04.05.2009
 * Time: 19:25:38
 *
 * Ответ на AdjustContestResponse
 */
public class AdjustContestResponse implements Response {

  /**
   * Информация о задачах соревнования. При запросе на изменение могли добавится новые задачи, поле problemIDs
   * содержит id всех задач, в частности только что созданные id новых задач. Если в запросе problems был null, в
   * этом поле тоже возвращается как null
   */
  @BinInfo(phpDefaultValue="array()")
  public int[] problemIDs;

}
