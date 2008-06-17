package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: Илья
 * Date: 25.05.2008
 * Time: 20:34:32
 */
public class ChangePasswordRequest implements InfoFrame {

    public String sessionID;
    public String oldPassword;
    public String newPassword;

}
