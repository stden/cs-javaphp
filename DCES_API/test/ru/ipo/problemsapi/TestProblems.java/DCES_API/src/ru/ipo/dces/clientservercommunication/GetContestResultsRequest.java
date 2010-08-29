package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: ����������
 * Date: 23.03.2009
 * Time: 21:47:49
 *
 * ������ ����������� ������������. ���������, ������� ����� ����� �� ���������� ����� �������,
 * ����������� � ContestDescription.resultAcessPolicy
 */
public class GetContestResultsRequest implements Request {

  /**
   * ������������� ������ ��������� ��� null, ���� ������ ���������� ��������
   */
  @PHPDefaultValue("null")
  public String sessionID;

  /**
   * ������������ ������������, ���������� �������� ��������� ��������. ��������� ��� ���������� ������� � ���
   * ������� �� �������������� �������. �������� ��� ������������� ������������ ������ ������� -1 ��� id ������
   * ������������
   */
  @PHPDefaultValue("null")
  public int contestID;    

}
