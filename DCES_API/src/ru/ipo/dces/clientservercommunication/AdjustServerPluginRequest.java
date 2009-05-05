package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 04.05.2009
 * Time: 1:38:00
 */
public class AdjustServerPluginRequest implements Request {

  public String sessionID;
  public String pluginAlias;
  public byte[] pluginData;
  public String description;

}

