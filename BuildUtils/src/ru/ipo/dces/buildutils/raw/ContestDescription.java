package ru.ipo.dces.buildutils.raw;

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
    @BinInfo(
            phpDefaultValue="array()",
            editable = false,
            defaultValue = "-1"            
    )
    public int contestID;

    /**
     * Название соревнования
     */
    @BinInfo(
            phpDefaultValue="array()",
            defaultValue = "Новое соревнование",
            title = "Название"
    )
    public String name;

    /**
     * Расширенное описание соревнования
     */
    @BinInfo(
            phpDefaultValue="array()",
            defaultValue = "",
            title = "Описание"
    )
    public String description;

    /**
     * Время начала соревнования. До этого времени подключение участников к соревнованию
     * физически невозможно
     */
    @BinInfo(
            phpDefaultValue="array()",
            defaultValue = BinInfo.NEW_INSTANCE_DEFAULT_VALUE,
            title = "Начало соревнования"
    )
    public Date start;

    /**
     * Время окончания соревнования. После этого времени участники могут подключаться к соревнованию,
     * но отсылка решений более невозможна. (Под отсылкой решений понимаются запросы SubmitSolutinRequest, которые
     * при обработке плагином стороны сервера помечаются плагином как "решение")
     */
    @BinInfo(
            phpDefaultValue="array()",
            defaultValue = BinInfo.NEW_INSTANCE_DEFAULT_VALUE,
            title = "Конец соревнования"
    )
    public Date finish;

    /**
     * Способ регистрации на соревнование
     */
    @BinInfo(
            phpDefaultValue="array()",
            defaultValue = "ByAdmins",
            title = "Тип регистрации"
    )
    public RegistrationType registrationType;

    /**
     * Набор данных об участнике
     */
    @BinInfo(
            phpDefaultValue="array()",
            defaultValue = BinInfo.NEW_INSTANCE_DEFAULT_VALUE,
            title = "Поля о пользователе"
    )
    public UserDataField[] data;

    /**
     * Настройка прав доступа к результатам соревнования. Описывает, когда
     * участникам и анонимным пользователям разрешен доступ к результатам соревнования.
     * Администраторы всегда имеют доступ к результатам
     */
    @BinInfo(
            phpDefaultValue="",
            defaultValue = BinInfo.NEW_INSTANCE_DEFAULT_VALUE,
            title = "Ружим доступа к результатам"
    )
    public ResultsAccessPolicy resultsAccessPolicy;

    /**
     * Тонкая настройка времени проведения соревнования
     */
    @BinInfo(
            phpDefaultValue="",
            defaultValue = BinInfo.NEW_INSTANCE_DEFAULT_VALUE,
            title = "Время соревнования"
    )
    public ContestTiming contestTiming;

    /**
     * Настройки задач по умолчанию
     */
    @BinInfo(
            phpDefaultValue="",
            defaultValue = BinInfo.NEW_INSTANCE_DEFAULT_VALUE,
            title = "Настройки задач"
    )
    public ContestSpecificSettings problemsDefaultSettings;

}
