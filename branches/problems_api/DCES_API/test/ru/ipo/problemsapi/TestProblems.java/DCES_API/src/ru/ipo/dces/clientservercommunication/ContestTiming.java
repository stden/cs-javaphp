package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: ����������
 * Date: 23.03.2009
 * Time: 19:49:40
 *
 * ��������� ������� ���������� ������������.
 */
public class ContestTiming {

  /**
   * <p>� ��������� ������� ���������� ������������ ������ ���� �����. ���� ����� ������ � ��������� ����������� (��.
   * ���� start � finish � ContestDescription). ���� ������������ �������� ��� �������� � ������
   * ������� ����������� � ������������. � ���� ������ ������������ �������������, ���� ����� �������
   * maxContestDuration �����, ���� ����� �������� ��� �������� �������� ������������ �������� StopContestRequest.
   * <p>� ����� ������� ���� start � finish �� ContestDescription ������������ ����������� ������������ �� �������
   * ������� ����������� (start) � ������� ������� ������� (finish)
   */
  @PHPDefaultValue("false")
  public boolean selfContestStart;

  /**
   * <p>������������ ����� ������������ � �������. ������������ ������ ���� selfContestStart == true.
   * <p>������ ������� ������������ ���������� ����� ����� ������� ������������� ��������� � ������������.
   * ����� ���� ��� maxContestDuration ����� ������, �������� ������� �������� �� �����.
   * (��� �������� ������� ���������� ������� SubmitSolutinRequest, �������
   * ��� ��������� �������� ������� ������� ���������� �������� ��� "�������")
   */
  @PHPDefaultValue("60")
  public int maxContestDuration;

  /**
   * <p>������ ��������� ������������. ����� � ������� �� ����� ��������, ������������� � �������� �������.
   * ��������������� �����. ��������� ������������ ������������ ������ ���� selfContestStart = false.
   * <p>����������� ������� "��������� ������������" ��. � ResultsAccessPolicy.contestEndingPermission
   */
  @PHPDefaultValue("15")
  public int contestEndingStart;

  /**
   * <p>����� ��������� ��������, ����� � ������� �� ����� ��������. ��������������� �����
   * ��������� �������� ������������ ������ ���� selfContestStart = false
   * <p>����������� ������� "��������� ������������" ��. � ResultsAccessPolicy.contestEndingPermission
   */
  @PHPDefaultValue("15")
  public int contestEndingFinish;

}