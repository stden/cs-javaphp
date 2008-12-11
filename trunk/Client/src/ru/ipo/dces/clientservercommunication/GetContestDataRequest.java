package ru.ipo.dces.clientservercommunication;

import ru.ipo.dces.client.Controller;

/**
 * Запрос: Получить данные о контесте
 */
public class GetContestDataRequest implements Request {
  
  public enum InformationType {
    NoInfo,          //не посылать вообще условия задачи
    ParticipantInfo, //посылать условия задачи для конктретного участника
    AdminInfo,       //посылать данные, формирующие условие и ответ к задаче
  }

  public String sessionID;

  /**
   * ID контеста. Вообще-то он содержится в sessionID, но Super Admin должен быть способен узнать данные по любому контесту
   */
  public int contestID;

  public InformationType infoType; 

  /**
   * Для задач с этими id требуется прислать расширенные данные. null - значит данные для всех задач
   * Если данные не нужны, information type требуется выбрать как NoInfo
   */
   public int[] extendedData;                                                                                             
}
