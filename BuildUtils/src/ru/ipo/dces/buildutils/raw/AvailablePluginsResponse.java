package ru.ipo.dces.buildutils.raw;

/**
 * Created by IntelliJ IDEA.
 * User: Ilya
 * Date: 14.11.2009
 * Time: 2:50:45
 *
 * Ответ на запрос о доступных в системе плагинах
 */
public class AvailablePluginsResponse implements Response {

  /**
   * Массив алиасов плагинов
   */
  @BinInfo(phpDefaultValue="array()")
  public String[] aliases;

  /**
   * Массив описаний плагинов
   */
  public String[] descriptions;

}
