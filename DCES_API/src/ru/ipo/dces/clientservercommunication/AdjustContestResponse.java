package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: ѕосетитель
 * Date: 04.05.2009
 * Time: 19:25:38
 *
 * ќтвет на AdjustContestResponse
 */
public class AdjustContestResponse implements Response {

  /**
   * »нформаци€ о задачах соревновани€. ѕри запросе на изменение могли добавитс€ новые задачи, поле problemIDs
   * содержит id всех задач, в частности только что созданные id новых задач
   */
  public int[] problemIDs;

}
