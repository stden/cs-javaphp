package ru.ipo.dces.clientservercommunication;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: ilya
 * Date: 12.10.2010
 * Time: 22:20:06
 */
public class CheckerRequest implements Request {

    @PHPDefaultValue("null")
    public String sessionID;

    @PHPDefaultValue("0")
    public int submissionID;

    @PHPDefaultValue("array()")
    public HashMap<String, String> result;
}
