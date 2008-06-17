package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: Илья
 * Date: 21.05.2008
 * Time: 14:23:45
 */
public class GetContestDataResponse implements InfoFrame {
    //информация о контесте, данные содержатся в массивах, одна запись для каждой задачи

    public ProblemDescription problems[];
    public ContestDescription contest;

}
