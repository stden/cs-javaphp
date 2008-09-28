package ru.ipo.dces.tests;

import java.awt.Component;

import javax.swing.*;

import org.junit.Test;

import ru.ipo.dces.client.*;
import ru.ipo.dces.client.ClientDialog.OpenPanelAction;
import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.mock.MockServer;

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
  public void test1() throws Exception, RequestFailedResponse {
    // �������� �������� ��������������� � �������� ��� ��������� ���������
    // ���������. �� ������ ������ ������ ��������� ���������. ������� ������
    // ������.
    ClientDialog cd = new ClientDialog(new JFrame());
    // TODO: �������, ����� ������ ���������� �� ������
    cd.server = new MockServer();
    testContestList(cd);
    cd.server = new RealServer(TestHTTP.ServerURL);
    testContestList(cd);
  }

  @Test
  public void test2() throws Exception, RequestFailedResponse {
    // ��������� ������������ ����� ���������� �������.
    ClientDialog cd = new ClientDialog(new JFrame());
    cd.server = new MockServer();
    IServer server = cd.server;
    server.doRequest(new CreateContestRequest("Example contest #1"));
    AvailableContestsResponse acr = server
        .doRequest(new AvailableContestsRequest());
    ContestDescription contest = acr.contests[0];
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
    // RegisterForm rf = new RegisterForm(new JFrame());

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

  private void testContestList(ClientDialog cd) throws Exception,
      RequestFailedResponse {
    IServer server = cd.server;
    server.doRequest(new CreateContestRequest("Example contest #1"));
    server.doRequest(new CreateContestRequest("Example contest #2"));
    ContestDescription[] contestList = server
        .doRequest(new AvailableContestsRequest()).contests;
    assertEquals("Example contest #1", contestList[0].name);
    assertEquals("Example contest #2", contestList[1].name);
  }

}
