package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: Илья
 * Date: 11.01.2009
 * Time: 18:34:37
 */
public class AdjustUserDataRequest implements Request {

  public String sessionID;
  public int userID;
  public String[] userData;

}
