package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: »ль€
 * Date: 21.05.2008
 * Time: 14:40:28
 */
public class SubmitSolutionRequest implements InfoFrame {

    public String sessionID;
    public String problemID; //номер задачи, по которой посылаетс€ решение
    public InfoFrame problemResult;

}
