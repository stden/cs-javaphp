package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 23.03.2009
 * Time: 21:47:49
 *
 * Запрос результатов соревнования. Участники, которые имеют право на выполнение этого запроса,
 * описываются в ContestDescription.resultAcessPolicy
 */
public class GetContestResultsRequest implements Request {

  /**
   * Идентификатор сессии участника или null, если запрос вызывается анонимно
   */
  @PHPDefaultValue("null")
  public String sessionID;

  /**
   * Идентификтор соревнования, результаты которого требуется получить. Актуально для анонимного запроса и для
   * запроса от администратора сервера. Участник или администратор соревнования должны указать -1 или id своего
   * соревнования
   */
  @PHPDefaultValue("null")
  public int contestID;    

}
