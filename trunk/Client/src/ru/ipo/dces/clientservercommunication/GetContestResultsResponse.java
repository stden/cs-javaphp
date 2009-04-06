package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 23.03.2009
 * Time: 21:51:20
 */
public class GetContestResultsResponse implements Response {

  /**
   * Заголовок таблицы
   * сначала информация об участнике, потом
   */
  public String[] headers;

  /**
   * Малые заголовки таблицы
   * id задачи -> массив заголовком столбцов
   */
  public String[][] minorHeaders;

  /**
   * Значения элементов таблицы
   * id участника -> id задачи -> массив значений в столбцах
   */
  public String[][][] table;

}
