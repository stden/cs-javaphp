package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: Илья
 * Date: 21.05.2008
 * Time: 21:58:03
 */
public class AdjustContestRequest implements InfoFrame {

    public String sessionID;
    public ContestDescription contest;
    public ProblemDescription[] problems;

}
