package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: ����������
 * Date: 23.03.2009
 * Time: 19:24:14
 */
public class ResultsAccessPolicy {

  public enum AccessPermission {
    /**
     * ��� ������� � �����������
     */
    NoAccess,
    /**
     * ������ ����������� ����������
     */
    OnlySelfResults,
    /**
     * ������ ������ � �����������
     */
    FullAccess,
  }

  /**
   * ����� �� ������ �� ����� ��������
   */
  public AccessPermission contestPermission;
  /**
   * ����� �� ������ �� ����� ��������� ��������
   */
  public AccessPermission contestEndingPermission;
  /**
   * ����� �� ������ ����� ��������
   */
  public AccessPermission afterContestPermission;

  /**
   * ����������������� ��������� ��������, � �������
   */
  public int contestEndingDuration;
  /**
   * ������ ��������� ��������, ����� � ������� �� ����� �������� � �������� �������
   */
  public int contestEndingStart;

}
