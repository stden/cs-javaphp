package ru.ipo.dces.buildutils.raw;

/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 16.04.2009
 * Time: 20:00:46
 *
 * Запрос на досрочное завершение соревнование. Возможен только если в описании соревнования
 * ContestDescription указано contestTiming.selfContestStart == true
 */
public class StopContestRequest implements Request {
  /**
   * Идентификатор сессии участника
   */
  @BinInfo(phpDefaultValue="null")
  public String sessionID;

}
