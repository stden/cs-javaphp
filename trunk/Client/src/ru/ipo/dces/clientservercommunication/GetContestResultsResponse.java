package ru.ipo.dces.clientservercommunication;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: ����������
 * Date: 23.03.2009
 * Time: 21:51:20
 */
public class GetContestResultsResponse {

  /**
   * ��������� �������
   * id ������ -> ��� ������
   */
  public HashMap<Integer, String> headers;

  /**
   * ����� ��������� �������
   * id ������ -> ������ ���������� ��������
   */
  public HashMap<Integer, String[]> minorHeaders;

  /**
   * �������� ��������� �������
   * id ��������� -> id ������ -> ������ �������� � ��������
   */
  public HashMap<Integer, HashMap<Integer, String[]>> table;

  /**
   * ���������� �����������
   * id ��������� -> ����� �� ������� � �������
   */
  public HashMap<Integer, Integer> sorting;

}
