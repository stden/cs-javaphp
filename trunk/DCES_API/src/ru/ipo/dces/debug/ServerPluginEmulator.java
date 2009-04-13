package ru.ipo.dces.debug;

import java.util.HashMap;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: ����
 * Date: 12.12.2008
 * Time: 20:39:50
 *
 * ���� ��������� ������������� ������� ������� �������. ��� ����� �����������, ���� ���������� �����������
 * ������ ������� ������� ��� ����������� � ��������� �������.
 */
public interface ServerPluginEmulator {
  /**
   * <p>����� ���������� ��� �������� ������� ���������. �� ���� �� �������� �������, � ���������� ��������� ��������.
   * ��� ������� ��������� � ��������� �������� ����� ���� � ��� ��
   * ��� - ��� <tt>HashMap String String</tt>
   * <p>������ ������ ������� ��������� ����� �������� "��������� ������� �����" ���� null, ����
   * ����� ���������� ������ ���. ��������� ���������� � ��� ������, ����, ��������, ���� ���������
   * ������������ �������� ������� ������ ������ ��� ������������ ����� ���. � ���� ������ ��������� �����
   * ��������� ����� ������� �������, � ���� ��������� ��� ���� ����� ���� ������� �������, �������� �����
   * ������ �� ������.
   * <p>������-�� �������� ��������� ������ �������� �� ���� ��� � ����� � ������, ����� �����, � ��� ����������
   * ������� ���������. �� ����� ��� �������� ����� ��������������, ������� � ���������� ������ checkSolution(). 
   * @param solution ������� ���������
   * @param result ���������� ������ <tt>HashMap</tt>, ������� ���������� ��������� ����������� ��������
   * @param state ��������� ������ ��� ������� ���������. ��� ������ ������ �������� ������� ����� null
   * @return ����� ��������� �������
   */
  public Object checkSolution(HashMap<String, String> solution, HashMap<String, String> result, Object state);

  /**
   * <p>����� ���������� �������, ��� �������� ������� ������. ������ ������� ������� ������ ��� �����,
   * ��� ���������� ����� � �������� � ������ �����������.
   * <p>����� ���������� ����� ���� ��� ��� �������� �������. �������, ���� �������������� ������ ����������
   * ������ ������ ������� �����, ����� ����� ������� ��������� ��������� �������, � ����� ���������� �������,
   * � ������� ��� �����. 
   * @return ������� � �������� ������
   */
  public File getStatement();

}