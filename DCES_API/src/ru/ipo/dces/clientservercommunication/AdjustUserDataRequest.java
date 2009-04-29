package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: Илья
 * Date: 11.01.2009
 * Time: 18:34:37
 */
public class AdjustUserDataRequest implements Request {

    public int userID;
    public String sessionID;

    public String login;
    public String password;
    public UserDescription.UserType newType;
    public String[] userData;
}
