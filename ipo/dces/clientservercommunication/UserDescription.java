package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: ����
 * Date: 21.05.2008
 * Time: 13:57:24
 */
public class UserDescription implements InfoFrame {

    public String login;
    public String password;
    public String name; //��� ���������
    public String institution; //�����������, �.�. �����, ����������� � �.�.
    //����� ���� ��������� �������������� ����. ��� �� ����� ������� ������� �������������, �� ���� ���

    public String extraInformation; //����� ������ ���� ����������� �������� �������������� ����������, ����
                                    //��� ������-�� �� ������ � ����� ����������� �����
}
