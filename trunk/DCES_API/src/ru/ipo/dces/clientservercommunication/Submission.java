package ru.ipo.dces.clientservercommunication;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: ilya
 * Date: 29.08.2010
 * Time: 23:52:16
 */
public class Submission {

    /**
     * id посылки
      */
    public int id;

    /**
     * Номер посылки, внутри одной задачи и одного участника
     */
    public int number;

    /**
     * Ответ участника
     */
    public HashMap<String, String> answer;

    /**
     * Результат проверки решения
     */
    public HashMap<String, String> result;
}
