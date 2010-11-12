package ru.ipo.dces.buildutils.raw;

import java.util.Date;

/**
 * Ответ: присоединились к соревнованию
 */
public class ConnectToContestResponse implements Response {

  /**
   * Возвращает идентификатор сессии, он используется во всех запросах, которые совершаются неанонимно
   */
  public String          sessionID;

  /** Описание пользователя, подключившегося к соревнования */
  public UserDescription user;

  /**
   * <p>Время окончания соревнования.
   * <p>Актуально в случае, если участник
   * сам начинает соревнование, т.е. в описании соревнования установлено contestTiming.selfContestStart == true
   */
  public Date finishTime; 
}
