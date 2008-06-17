package ru.ipo.dces.clientservercommunication;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ����
 * Date: 21.05.2008
 * Time: 15:09:41
 */
public class ContestDescription implements InfoFrame {
                
    public int contestID;

    //�������� ��������� ���������
    public String name;

    //�������� ��������
    public String description;

    //����� ������ ��������
    public Date start;

    //����� ��������� ��������
    public Date finish;

    //�������� �������� - ������ �����������.
    // 0 - ����� ���������������� ������ � ������� �������
    // 1 - ����������� ������ ��������������
    //-1 - �������� �� �����������
    public int registrationType;

    // 0 - ���������
    // 1 - �������
    //-1 - �������� �� �����������    
    public int visible; //�������� �� ����� ��������� �� ������ ���� ������, ��������� ����� �������� ���������

}
