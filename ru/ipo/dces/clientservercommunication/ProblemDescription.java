package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: ����
 * Date: 21.05.2008
 * Time: 15:20:38
 */
public class ProblemDescription implements InfoFrame {

    public String id; //������������� ������
    public String clientPluginID; //ID �������, ������� ����� ������������ ������
    public String serverPluginID; //ID �������, ������� ������� �������, �������� ���������� �� ������
    public String name; //��������
    public byte[] problemData; //������� ������. ��������� �����, ��� �����, ������� ����� ������������� ������������ � �������, ��������������� ������

}
