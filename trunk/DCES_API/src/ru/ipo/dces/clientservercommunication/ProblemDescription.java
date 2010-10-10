package ru.ipo.dces.clientservercommunication;

/**
 * �������� ������. �� �������� �������� ��� �������, �� ������������ � ���
 */
public class ProblemDescription {

    /**
     * ������������� ������
     */
    @PHPDefaultValue("-1")
    public int id;

    /**
     * Binary data with ZipProblem
     */
    @PHPDefaultValue("null")
    public byte[] problem;

    @PHPDefaultValue("")
    public ContestSpecificSettings settings;

    /**
     * ������������� ������� ������� �������, � ������� �������� �������� ����� ������ ������
     *
     * @deprecated ������������ ���� problem
     */
    @PHPDefaultValue("'Test client plugin'")
    public String clientPluginAlias;

    /**
     * ������������� ������� ������� �������, ������� ����� ������������ ������ ���������
     *
     * @deprecated ������������ ���� problem
     */
    @PHPDefaultValue("'Test server plugin'")
    public String serverPluginAlias;

    /**
     * �������� ������
     *
     * @deprecated ������������ ���� problem
     */
    @PHPDefaultValue("'Test problem'.rand(0,239239)")
    public String name;

    /**
     * ������� ������. ��� ���������� ������� ������ ��� ���������� ���������. ���� ����������� �������� � ���
     * �������, � �������� ������� ���� ������������
     *
     * @deprecated ������������ ���� problem
     */
    @PHPDefaultValue("null")
    public byte[] statement;

    /**
     * ������� ������. ��� ������, ������� ������������ ��� �������� �������.
     *
     * @deprecated ������������ ���� problem
     */
    @PHPDefaultValue("null")
    public byte[] statementData;

    /**
     * ����� � ������. ��� ������, ������� ������������ ��� �������� ������
     *
     * @deprecated ������������ ���� problem
     */
    @PHPDefaultValue("null")
    public byte[] answerData;

}
