package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: Илья
 * Date: 18.12.2008
 * Time: 23:30:51
 */
public class AdjustClientPluginRequest implements Request {

  public String sessionID;
  public String pluginAlias;
  public byte[] pluginData;
  public String description;

}
