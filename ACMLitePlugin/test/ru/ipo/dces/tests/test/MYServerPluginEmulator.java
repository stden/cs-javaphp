package ru.ipo.dces.tests.test;

import ru.ipo.dces.debug.ServerPluginEmulator;

import java.util.HashMap;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 25.04.2009
 * Time: 15:37:53
 * To change this template use File | Settings | File Templates.
 */
   public class MYServerPluginEmulator implements ServerPluginEmulator {

      //��������� �����
      public static final String RIGHT_ANSWER = "4gggggggg";

      //����� �������� �������.
      //�������� �� ���� solution �������,  result - ������ ���, ������� ���� ��������� ����������� �������� � state - ������������ ������ ���������. �� ����� ������������ ���������, ����� ������� ���� �����, ���������� ���, ������� ���������� �������
      //���������� ���������� ����� ��������� ������
      public Object checkSolution(HashMap<String, String> solution, HashMap<String, String> result, Object state) {

        //�������� ��������� � ����, ������� ���������� ��� �������� ���������
        Integer intState = (Integer)state;

        //��������� ����� ������� ������� �������
        //��� ������ ������ ������ state = null
        if (intState == null)
          intState = 1;
        else
          intState ++ ;

        if (intState <= 3) {
          //���� �������� �� ����� ��� ������ ���, �� ����� ��������� �������
          String answer = solution.get("answer");
          //���������� � ���������� �������
          if (RIGHT_ANSWER.equals(answer))
            result.put("result", "yes");
          else
            result.put("result", "no");
        }
        else {
          //���� �������� ����� ��� ������ ���, ��������, ��� ��������� �� ����
          result.put("result", "no more submissions");
        }

        //���������� ����� ���������
        return intState;
      }

      //� ���� ������ ���������� ������� ������� ������ � ������� �������, � ������� ��� �����
      //����� �������, ��� ������� ��� ����� � �������� C://dces/problem_folder, ��� �� ���� ���������, ���������� ������� �������
      public File getStatement() {
        return new File("C://dces/problem_folder");
      }
    }