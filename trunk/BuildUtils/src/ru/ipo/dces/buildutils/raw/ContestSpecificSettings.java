package ru.ipo.dces.buildutils.raw;

/**
 * Created by IntelliJ IDEA.
 * User: ilya
 * Date: 05.09.2010
 * Time: 15:51:09
 */
public class ContestSpecificSettings {

    public enum TableResultChoice {
        Best,
        Last
    }

    /**
     * Максимальное количество посылок задачи
     */
    @BinInfo(
            phpDefaultValue="1000",
            defaultValue = "1000",
            title = "Количество отсылок"
    )
    public int sendCount;

    /**
     * Преобразование результатов в таблицу
     */
    @BinInfo(
            phpDefaultValue="''",
            defaultValue = "",
            title = "Перенос результата"
    )
    public String resultTransition;


    /**
     * Выбор результата для таблицы
     */
    @BinInfo(
            phpDefaultValue="'Best'",
            defaultValue = "Best",
            title = "Выбор результата"
    )
    public TableResultChoice tableResultChoice;
}