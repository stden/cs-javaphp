package ru.ipo.dces.clientservercommunication;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: ilya
 * Date: 29.08.2010
 * Time: 23:52:16
 */
public class Submission {

    /**
     * id �������
      */
    public int id;

    /**
     * ����� �������, ������ ����� ������ � ������ ���������
     */
    public int number;

    /**
     * ����� ���������
     */
    public HashMap<String, String> answer;

    /**
     * ��������� �������� �������
     */
    public HashMap<String, String> result;
}
