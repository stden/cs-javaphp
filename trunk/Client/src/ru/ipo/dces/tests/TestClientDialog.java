package ru.ipo.dces.tests;

import java.awt.Component;
import java.util.List;

import javax.swing.*;

import org.junit.Test;

import ru.ipo.dces.client.*;
import ru.ipo.dces.client.ClientDialog.OpenPanelAction;


import static org.junit.Assert.*;

public class TestClientDialog {
  @Test
  public void baseTest() {
    // ��� ������ ������� ���������� ���������� ����
    ClientDialog cd = new ClientDialog(new JFrame());
    // � ���������� "DCES Client"
    assertEquals("DCES Client", cd.getTitle());
    // ���� ��������� �� ��� ����� � ������� SplitPane
    JSplitPane sp = (JSplitPane) cd.getContentPane().getComponent(0);
    assertNotNull(sp);
    // ����� � ������ ������
    JPanel leftPanel = (JPanel) sp.getComponent(1);
    assertNotNull(leftPanel);
    JPanel rightPanel = (JPanel) sp.getComponent(2);
    assertNotNull(rightPanel);
    // �� ����� ������ - ������ ��� ������ Plugin'�
    for (Component c : leftPanel.getComponents()) {
      JButton btn = (JButton) c;
      assertNotNull(btn);
      // ��� ������ ������ ������ ����������, ������� ��������� ������
      OpenPanelAction action = (OpenPanelAction) btn.getActionListeners()[0];
      assertNotNull(action);
    }
  }

  @Test
  public void test1() {
    // �������� �������� ��������������� � �������� ��� ��������� ���������
    // ���������. �� ������ ������ ������ ��������� ���������. ������� ������
    // ������.
    ClientDialog cd = new ClientDialog(new JFrame());
    cd.server = new MockServer();
    // TODO: �������, ����� ������ ���������� �� ������
    IServer server = cd.server;
    server.addContest("Example contest #1");
    server.addContest("Example contest #2");
    List<Contest> contestList = server.getAvaibleContests();
    assertEquals("Example contest #1", contestList.get(0).name);
    assertEquals("Example contest #2", contestList.get(1).name);
  }

  @Test
  public void test2() {
    // ��������� ������������ ����� ���������� �������.
    ClientDialog cd = new ClientDialog(new JFrame());
    cd.server = new MockServer();
    IServer server = cd.server;
    server.addContest("Example contest #1");
    Contest contest = server.getContest(0);
    assertEquals("Example contest #1", contest.name);
  }

  @Test
  public void test3() {
    // ��������� ������������ ����� ������������������ � ������� ��� ����������
    // ��������. �� ������ ������ ��������������� �����, ���� ����������� ���
    // ������� �������������� ��������, ������� ������ ��� �����, �� ������ ����
    // ������, ������� ��������� ������ �� ������������ � �������, �, � ������
    // ������, ������ ������ ��������� ��� ������� ���������. � ������ �������
    // ������� ������ ������������ �������� ������ ������.
  }

  @Test
  public void test4() {
    // �������� �������� ����� ����������� � ��������. �� �������� �� �������
    // ������ ��������. ������ ��������� ��-�������� ��������, ��� ��� �� �����
    // ������� ����� ������ �������, �� ��� ���� ����������� ���������
    // ���������� � ��������.
  }

  @Test
  public void test5() {
    // �������� �������� ����� ������ ������. �� �������� ������ �� ������ �����
    // �� �������� ��������, ������� ��������� ������ (��������� ��������
    // ��������), ����� ���������� ���������� � ����� ������: ������� ������,
    // ��������� ����� �������, ��������� �������� ������� �� ������, ���������
    // ��������� �������� ������� - � ������, ���� ������������� �������� �����
    // �����������). ����� ������� ���������� ������ ������ �����, ����������
    // ����� (���� ��� �������), ����������� ��������� � �������� ��������.
  }

}
