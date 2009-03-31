package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 23.03.2009
 * Time: 19:24:14
 */
public class ResultsAccessPolicy {

  public enum AccessPermission {
    /**
     * нет доступа к результатам
     */
    NoAccess,
    /**
     * только собственные результаты
     */
    OnlySelfResults,
    /**
     * полный доступ к результатам
     */
    FullAccess,
  }

  /**
   * права на доступ во время контеста
   */
  public AccessPermission contestPermission;
  /**
   * права на доступ во время окончания контеста
   */
  public AccessPermission contestEndingPermission;
  /**
   * права на доступ после контеста
   */
  public AccessPermission afterContestPermission;

  /**
   * Продолжительность окончания контеста, в минутах
   */
  public int contestEndingDuration;
  /**
   * Начало окончания контеста, время в минутах от конца контеста в обратную сторону
   */
  public int contestEndingStart;

}
