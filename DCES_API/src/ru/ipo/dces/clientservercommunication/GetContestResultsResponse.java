package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: ����������
 * Date: 23.03.2009
 * Time: 21:51:20
 *
 * ����� �� ������ � ��������� ����������� ������������
 */
public class GetContestResultsResponse implements Response {

  //TODO �������������� �������� � JavaDoc �����, ��� ������

  /**
   * <p>��������� �������. �������. ���������� ��������:
   * <p>������ ������� "admin info" - ����������� ���������� �� ���������, ������� ������������ ������
   * ��� ���������������
   * <p>������ ������� "participant" - ������ �� ���������, (��. UserDescription.dataValue)
   * <p>������ � ����� ������� - ���������� �� �������
   */
  public String[] headers;

  /**
   * ���������� ��������. ����������:
   * <p>���������� ������ ������� "admin info" - ��� "id" � "login", �.�. ������������� � ����� ������������
   * <p>���������� "participant" - ������ �� ���������, (��. UserDescription.dataValue)
   * ���� ������ ����������� �������� ��� �� ��������� ������������, �� ������������ ������ �� ������,
   * ������� �������� � UserDataField ��� showInResult == true
   * <p>���������� �������� � ����� �������� - ���������� � ������������ �� �������.
   */
  public String[][] minorHeaders;

  /**
   * ������ �� ����������� ��� ������� ���������
   */
  public String[][][] table;

  /**
   * ����� ������ �������, ������� ������������� ��������� �������. ��� ������� � �������������� �������
   * ������������ -1
   */
  public int userLine;

}