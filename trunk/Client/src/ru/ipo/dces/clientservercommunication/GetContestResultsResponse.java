package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: ����������
 * Date: 23.03.2009
 * Time: 21:51:20
 */
public class GetContestResultsResponse implements Response {

  /**
   * ��������� �������
   * ������� ���������� �� ���������, �����
   */
  public String[] headers;

  /**
   * ����� ��������� �������
   * id ������ -> ������ ���������� ��������
   */
  public String[][] minorHeaders;

  /**
   * �������� ��������� �������
   * id ��������� -> id ������ -> ������ �������� � ��������
   */
  public String[][][] table;

}
