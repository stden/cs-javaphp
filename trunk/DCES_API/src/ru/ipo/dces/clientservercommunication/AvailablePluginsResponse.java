package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: Ilya
 * Date: 14.11.2009
 * Time: 2:50:45
 *
 * Ответ на запрос о доступных в системе плагинах
 */
public class AvailablePluginsResponse {

  /**
   * Массив алиасов плагинов
   */
  public String[] aliases;

  /**
   * Массив описаний плагинов
   */
  public String[] descriptions;

}
