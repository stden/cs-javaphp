package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 23.03.2009
 * Time: 19:49:40
 *
 * Настройка времени проведения соревнования.
 */
public class ContestTiming {

  /**
   * <p>В отношении времени проведения соревнования бывают двух типов. Либо время начала и окончания фиксированы (см.
   * поля start и finish в ContestDescription). Либо соревнование начинает сам участник в момент
   * первого подключения к соревнованию. В этом случае соревнование заканчивается, либо когда пройдет
   * maxContestDuration минут, либо когда участник сам досрочно закончит соревнование запросом StopContestRequest.
   * <p>В обоих случаях поля start и finish из ContestDescription ограничивают возможности пользователя по времени
   * первого подключения (start) и времени отсылке решений (finish)
   */
  @BinInfo(
          phpDefaultValue="false",
          defaultValue = "false",
          title = "Самостоятельное начало"
  )
  public boolean selfContestStart;

  /**
   * <p>Максимальное время соревнования в минутах. Используется только если selfContestStart == true.
   * <p>Отсчет времени соревнования начинается сразу после первого подсоединения участника к соревнованию.
   * После того как maxContestDuration минут прошли, отсылать решения участник не может.
   * (Под отсылкой решений понимаются запросы SubmitSolutinRequest, которые
   * при обработке плагином стороны сервера помечаются плагином как "решение")
   */
  @BinInfo(
          phpDefaultValue="60",
          defaultValue = "60",
          title = "Максимальное время соревнования"
  )
  public int maxContestDuration;

  /**
   * <p>Начало окончания соревнования. Время в минутах от конца контеста, отсчитывается в обратную сторону.
   * Неотрицательное число. Окончание соревнования используется только если selfContestStart = false.
   * <p>Определение понятию "окончание соревнования" см. в ResultsAccessPolicy.contestEndingPermission
   */
  @BinInfo(
          phpDefaultValue="15",
          defaultValue = "0",
          title = "Начало окончания"
  )
  public int contestEndingStart;

  /**
   * <p>Конец окончания контеста, время в минутах от конца контеста. Неотрицательное число
   * Окончание контеста используется только если selfContestStart = false
   * <p>Определение понятию "окончание соревнования" см. в ResultsAccessPolicy.contestEndingPermission
   */
  @BinInfo(
          phpDefaultValue="15",
          defaultValue = "0",
          title = "Конец окончания"
  )
  public int contestEndingFinish;

}