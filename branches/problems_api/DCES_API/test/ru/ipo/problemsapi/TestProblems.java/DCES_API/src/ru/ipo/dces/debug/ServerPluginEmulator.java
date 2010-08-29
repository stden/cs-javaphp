package ru.ipo.dces.debug;

import ru.ipo.dces.exceptions.GeneralRequestFailureException;

import java.util.HashMap;
import java.io.File;
import java.io.IOException;

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
   * @throws ru.ipo.dces.exceptions.GeneralRequestFailureException if communication with a server failed
   */
  public abstract Object checkSolution(HashMap<String, String> solution, HashMap<String, String> result, Object state)
          throws GeneralRequestFailureException;

  /**
   * <p>����� ���������� �������, ��� �������� ������� ������. ������ ������� ������� ������ ��� �����,
   * ��� ���������� ����� � �������� � ������ �����������.
   * <p>����� ���������� ����� ���� ��� ��� �������� �������. �������, ���� �������������� ������ ����������
   * ������ ������ ������� �����, ����� ����� ������� ��������� ��������� �������, � ����� ���������� �������,
   * � ������� ��� �����. 
   * @return ������� � �������� ������
   * @throws ru.ipo.dces.exceptions.GeneralRequestFailureException if communication with a server failed
   * @throws java.io.IOException if failed to write folder with statement
   */
  public abstract File getStatement() throws GeneralRequestFailureException, IOException;

}
