package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: ilya
 * Date: 05.09.2010
 * Time: 15:51:09
 */
public class ContestSpecificSettings {

    public enum TableResultChoice {
        Best,
        Last
    }

    /**
     * ������������ ���������� ������� ������
     */
    @PHPDefaultValue("1000")
    public int sendCount;

    /**
     * �������������� ����������� � �������
     */
    @PHPDefaultValue("''")
    public String resultTransition;


    /**
     * ����� ���������� ��� �������
     */
    @PHPDefaultValue("'Best'")
    public TableResultChoice tableResultChoice;
}