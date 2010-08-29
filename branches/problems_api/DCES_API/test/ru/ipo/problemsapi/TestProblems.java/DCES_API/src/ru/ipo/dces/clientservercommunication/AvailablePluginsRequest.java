package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: Ilya
 * Date: 14.11.2009
 * Time: 2:33:33
 *
 * Запрос на выдачу плагинов стороны клиента или стороны сервера
 */
public class AvailablePluginsRequest implements Request {  

  @PHPDefaultValue("null")
  public String sessionID;

  /**
   * Выбор, какие плагины придут по запросу
   */
  @PHPDefaultValue("'Client'")
  public PluginSide pluginSide;

}
