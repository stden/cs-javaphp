package ru.ipo.dces.clientservercommunication;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 23.03.2009
 * Time: 21:51:20
 */
public class GetContestResultsResponse {

  /**
   * Заголовок таблицы
   * id задачи -> имя задачи
   */
  public HashMap<Integer, String> headers;

  /**
   * Малые заголовки таблицы
   * id задачи -> массив заголовком столбцов
   */
  public HashMap<Integer, String[]> minorHeaders;

  /**
   * Значения элементов таблицы
   * id участника -> id задачи -> массив значений в столбцах
   */
  public HashMap<Integer, HashMap<Integer, String[]>> table;

  /**
   * Сортировка результатов
   * id участника -> номер по порядку в таблице
   */
  public HashMap<Integer, Integer> sorting;

}
