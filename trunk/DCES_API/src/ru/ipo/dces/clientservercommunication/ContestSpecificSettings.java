package ru.ipo.dces.clientservercommunication;

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
    @PHPDefaultValue("1000")
    public int sendCount;

    /**
     * Преобразование результатов в таблицу
     */
    @PHPDefaultValue("''")
    public String resultTransition;


    /**
     * Выбор результата для таблицы
     */
    @PHPDefaultValue("'Best'")
    public TableResultChoice tableResultChoice;
}