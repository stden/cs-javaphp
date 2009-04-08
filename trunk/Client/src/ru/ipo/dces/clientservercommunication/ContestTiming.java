package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 23.03.2009
 * Time: 19:49:40
 */
public class ContestTiming {

  /**
   * возможность самостоятельно управлять временем контеста. Начинать и заканчивать его
   */
  public boolean selfContestStart;

  /**
   * максимальное время контеста в минутах, если он запускается самостоятельно. Контест заканчивается либо
   * после того как он остановлен, либо после того как он 
   */
  public int maxContestDuration;

  /**
   * Начало окончания контеста, время в минутах от конца контеста в обратную сторону. Неотрицательное число
   * Окончание контеста используется только если selfContestStart = false
   */
  public int contestEndingStart;

  /**
   * Конец окончания контеста, время в минутах от конца контеста. Неотрицательное число
   * Окончание контеста используется только если selfContestStart = false
   */
  public int contestEndingFinish;

}