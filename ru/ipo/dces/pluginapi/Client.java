package ru.ipo.dces.pluginapi;

import ru.ipo.dces.clientservercommunication.InfoFrame;

/**
 * Created by IntelliJ IDEA.
 * User: Илья
 * Date: 10.06.2008
 * Time: 1:13:17
 */
public interface Client {

//    Системные плагины (не задачи) можно реализовать самим и не отдавать
//    третьей стороне. Если третья сторона захочет писать системные плагины, в api надо будет добавить методы

    /**
     * Sends information to the server-side plugin
     * @param solution information to send
     * @return recieved information. May return RequestFailedResponse
     */
    public InfoFrame submitSolution(InfoFrame solution);

}
