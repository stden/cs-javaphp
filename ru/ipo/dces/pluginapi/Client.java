package ru.ipo.dces.pluginapi;

import ru.ipo.dces.clientservercommunication.InfoFrame;

/**
 * Created by IntelliJ IDEA.
 * User: ����
 * Date: 10.06.2008
 * Time: 1:13:17
 */
public interface Client {

//    ��������� ������� (�� ������) ����� ����������� ����� � �� ��������
//    ������� �������. ���� ������ ������� ������� ������ ��������� �������, � api ���� ����� �������� ������

    /**
     * Sends information to the server-side plugin
     * @param solution information to send
     * @return recieved information. May return RequestFailedResponse
     */
    public InfoFrame submitSolution(InfoFrame solution);

}
