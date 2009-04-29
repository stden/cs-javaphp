package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: ����������
 * Date: 23.03.2009
 * Time: 19:49:40
 */
public class ContestTiming {

  /**
   * ����������� �������������� ��������� �������� ��������. �������� � ����������� ���
   */
  public boolean selfContestStart;

  /**
   * ������������ ����� �������� � �������, ���� �� ����������� ��������������. ������� ������������� ����
   * ����� ���� ��� �� ����������, ���� ����� ���� ��� �� 
   */
  public int maxContestDuration;

  /**
   * ������ ��������� ��������, ����� � ������� �� ����� �������� � �������� �������. ��������������� �����
   * ��������� �������� ������������ ������ ���� selfContestStart = false
   */
  public int contestEndingStart;

  /**
   * ����� ��������� ��������, ����� � ������� �� ����� ��������. ��������������� �����
   * ��������� �������� ������������ ������ ���� selfContestStart = false
   */
  public int contestEndingFinish;

}