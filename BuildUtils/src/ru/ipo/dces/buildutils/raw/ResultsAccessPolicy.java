package ru.ipo.dces.buildutils.raw;

/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 23.03.2009
 * Time: 19:24:14
 *
 * Не является запросом или ответом, но используется внутри них
 */
public class ResultsAccessPolicy {

  /**
   * Права на доступ участников к результатам соревнования
   */
  public enum AccessPermission {
    /**
     * Нет доступа к результатам
     */
    NoAccess,
    /**
     * Только собственные результаты. Для анонимного пользователя доступа нет
     */
    OnlySelfResults,
    /**
     * Полный доступ к результатам. Для анонимного участника также подразумевается полный доступ 
     */
    FullAccess,
  }

  /**
   * права на доступ во время соревнования
   */
  @BinInfo(
          phpDefaultValue="'FullAccess'",
          defaultValue = "NoAccess",
          title="В течение соревнования"
  )
  public AccessPermission contestPermission;

  /**
   * <p>Права на доступ во время окончания соревнования.
   * <p>Окончание соревнования - это промежуток времени, расположенный в районе момента конца соревнования.
   * В этот промежуток времени можно настроить права на доступ к результатам соревнования, других назначений
   * у "окончания соревнования" нет.
   * <p>Например, в соревнованиях ACM результаты перестают быть видны участникам за час до конца, а становятся видны
   * уже через некоторое время после окончания соревнования, чтобы не портить интригу при награждении
   */
  @BinInfo(
          phpDefaultValue="'FullAccess'",
          defaultValue = "NoAccess",
          title="В течение окончания"
  )
  public AccessPermission contestEndingPermission;

  /**
   * права на доступ после соревнования
   */
  @BinInfo(
          phpDefaultValue="'FullAccess'",
          defaultValue = "NoAccess",
          title="После соревнования"
  )
  public AccessPermission afterContestPermission;  

}
