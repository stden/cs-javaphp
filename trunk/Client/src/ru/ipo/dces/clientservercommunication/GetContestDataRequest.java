package ru.ipo.dces.clientservercommunication;

import ru.ipo.dces.client.Controller;

/**
 * «апрос: ѕолучить данные о контесте
 */
public class GetContestDataRequest implements Request {
  public String sessionID;

  /**
   * ID контеста, естественно, содержитс€ в sessionID, но Super Admin должен быть способен узнать данные по любому контесту
   */
  public String contestID;

  /**
   * ƒл€ задач с этими id требуетс€ прислать расширенные данные - только дл€ администраторов
   */
  public int[] extendedData;

  public GetContestDataRequest() {
    sessionID = Controller.sessionID;
  }
}
