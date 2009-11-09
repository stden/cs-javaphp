package ru.ipo.dces.clientservercommunication;

/**
 * «апрос: ѕолучить полные данные о соревновании
 */
public class GetContestDataRequest implements Request {

  /**
   * “ип информации, которую требуетс€ получить при запросе
   */
  public enum InformationType {
    /**
     * ƒанные по задачам посылать не требуетс€
     */
    NoInfo,
    /**
     * ѕослать услови€ задач дл€ участника, выполн€ющего запрос
     */
    ParticipantInfo,
    /**
     * ѕослать данные, использующиес€ дл€ создани€ услови€ и ответов к задаче. Ётот тип информации может
     * получить только администратор соревновани€ или сервера
     */
    AdminInfo,
  }

  /**
   * »дентификатор сессии, может быть null, если запрос выполн€етс€ анонимно
   */
  @PHPDefaultValue("null")
  public String sessionID;

  /**
   * ID соревновани€, по которому производитс€ запрос. јктуален дл€ администратора сервера и дл€ анонима.
   * ”частник соревновани€ и админитратор соревновани€ должны указать -1 или id своего соревновани€
   */
  @PHPDefaultValue("null")
  public int contestID;

  /**
   * “ип запрашиваемой информации
   */
  @PHPDefaultValue("'ParticipantInfo'")
  public InformationType infoType; 

  /**
   * ƒл€ задач с этими id требуетс€ прислать расширенные данные. null - значит данные дл€ всех задач
   * ≈сли данные не нужны, information type требуетс€ выбрать как NoInfo
   */
   @PHPDefaultValue("array()")
   public int[] extendedData;                                                                                             
}
