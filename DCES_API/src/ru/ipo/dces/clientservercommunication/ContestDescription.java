package ru.ipo.dces.clientservercommunication;

import java.util.Date;

/**
 * Описание соревнования. Не является запросом, входит как составная часть в различные запросы и ответы сервера.
 */
public class ContestDescription {

    /**
     * Тип регистрации на соревнование. Регистрироваться можно либо самостоятельно, либо только через администратора
     * соревнования (сервера)
     */
    public static enum RegistrationType {

        /**
         * Можно регистрироваться самому с помощью запроса RegisterToContestRequest
         */
        Self,
        /**
         * Регистриуют пользователей только администраторы
         */
        ByAdmins,
    }

    /**
     * ID соревнования
     */
    @PHPDefaultValue("-1")
    @BinInfo(editable = false)
    public int contestID;

    /**
     * Название соревнования
     */
    @PHPDefaultValue("'Sample contest'")
    @BinInfo(title = "Название")
    public String name;

    /**
     * Расширенное описание соревнования
     */
    @PHPDefaultValue("'Description of sample contest'")
    @BinInfo(title = "Описание")
    public String description;

    /**
     * Время начала соревнования. До этого времени подключение участников к соревнованию
     * физически невозможно
     */
    @PHPDefaultValue("time()")
    @BinInfo(title = "Начало")
    public Date start;

    /**
     * Время окончания соревнования. После этого времени участники могут подключаться к соревнованию,
     * но отсылка решений более невозможна. (Под отсылкой решений понимаются запросы SubmitSolutinRequest, которые
     * при обработке плагином стороны сервера помечаются плагином как "решение")
     */
    @PHPDefaultValue("time() + 3600")
    @BinInfo(title = "Окончание")
    public Date finish;

    /**
     * Способ регистрации на соревнование
     */
    @PHPDefaultValue("'ByAdmins'")
    @BinInfo(title = "Тип регистрации")
    public RegistrationType registrationType;

    /**
     * Набор данных об участнике
     */
    @PHPDefaultValue("array()")
    @BinInfo(title = "Поля о пользователе")
    public UserDataField[] data;

    /**
     * Настройка прав доступа к результатам соревнования. Описывает, когда
     * участникам и анонимным пользователям разрешен доступ к результатам соревнования.
     * Администраторы всегда имеют доступ к результатам
     */
    @PHPDefaultValue("")
    @BinInfo(title = "Ружим доступа к результатам")
    public ResultsAccessPolicy resultsAccessPolicy;

    /**
     * Тонкая настройка времени проведения соревнования
     */
    @PHPDefaultValue("")
    @BinInfo(title = "Время соревнования")
    public ContestTiming contestTiming;

    /**
     * Настройки задач по умолчанию
     */
    @PHPDefaultValue("")
    @BinInfo(title = "Настройки задач")
    public ContestSpecificSettings problemsDefaultSettings;

}
