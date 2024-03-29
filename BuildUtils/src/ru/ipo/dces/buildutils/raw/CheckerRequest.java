package ru.ipo.dces.buildutils.raw;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: ilya
 * Date: 12.10.2010
 * Time: 22:20:06
 */
public class CheckerRequest implements Request {

    @BinInfo(phpDefaultValue="null")
    public String sessionID;

    @BinInfo(phpDefaultValue="0")
    public int submissionID;

    @BinInfo(phpDefaultValue="array()")
    public HashMap<String, String> result;
}
