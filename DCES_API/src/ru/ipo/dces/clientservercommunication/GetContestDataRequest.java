package ru.ipo.dces.clientservercommunication;

/**
 * Запрос: Получить полные данные о соревновании
 */
public class GetContestDataRequest implements Request {

  /**
   * Тип информации, которую требуется получить при запросе
   */
  public enum InformationType {
    /**
     * Данные по задачам посылать не требуется
     */
    NoInfo,
    /**
     * Послать условия задач для участника, выполняющего запрос
     */
    ParticipantInfo,
    /**
     * Послать данные, использующиеся для создания условия и ответов к задаче. Этот тип информации может
     * получить только администратор соревнования или сервера
     */
    AdminInfo,
  }

  /**
   * Идентификатор сессии, может быть null, если запрос выполняется анонимно
   */
  @PHPDefaultValue("null")
  public String sessionID;

  /**
   * ID соревнования, по которому производится запрос. Актуален для администратора сервера и для анонима.
   * Участник соревнования и админитратор соревнования должны указать -1 или id своего соревнования
   */
  @PHPDefaultValue("null")
  public int contestID;

  /**
   * Тип запрашиваемой информации
   */
  @PHPDefaultValue("'ParticipantInfo'")
  public InformationType infoType; 

  /**
   * Для задач с этими id требуется прислать расширенные данные. null - значит данные для всех задач
   * Если данные не нужны, information type требуется выбрать как NoInfo
   */
   @PHPDefaultValue("array()")
   public int[] extendedData;                                                                                             
}
