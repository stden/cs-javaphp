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

  /** ������������ ������� � ������ �� ����� ������ */
  @Test
  public void baseTest() {
    ClientData.server = new MockServer();
    // ��� ������ ������� ���������� ���������� ����
    ClientDialog cd = new ClientDialog(new JFrame());
    // � ���������� "DCES Client"
    assertEquals("DCES Client", cd.getTitle());
    // ���� ��������� �� ��� ����� � ������� SplitPane
    JSplitPane sp = (JSplitPane) cd.getContentPane().getComponent(0);
    assertNotNull(sp);
    // ����� � ������ ������
    JPanel leftPanel = (JPanel) sp.getComponent(2);
    assertNotNull(leftPanel);
    assertEquals("Left panel", leftPanel.getName());
    JPanel rightPanel = (JPanel) sp.getComponent(1);
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

  /**
   * �������� �������� ��������������� � �������� ��� ��������� ���������
   * ���������. �� ������ ������ ������ ��������� ���������. ������� ������
   * ������.
   */
  @Test
  public void test1() throws Exception, RequestFailedResponse {
    ClientData.server = new MockServer();
    testContestList(new ClientDialog(new JFrame()));
    ClientData.server = new RealServer(TestHTTP.ServerURL);
    testContestList(new ClientDialog(new JFrame()));
    // ������ ���������� �� ������
    ClientDialog cd = new ClientDialog(new JFrame());
    cd.adminPanel.reloadButton.getActionListeners()[0].actionPerformed(null);
    // ���������, ��� �������� � ���������
    cd.adminPanel.contestList.setSelectedIndex(0);
    ContestDescription cc = (ContestDescription) cd.adminPanel.contestList
        .getSelectedValue();
    assertEquals("Example contest #1", cc.name);
  }

  /** ��������� ������������ ����� ���������� �������. */
  @Test
  public void test2() throws Exception, RequestFailedResponse {
    new ClientDialog(new JFrame());
    ClientData.server = new MockServer();
    ClientData.server.doRequest(new CreateContestRequest("Example contest #1"));
    AvailableContestsResponse acr = ClientData.server
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

  private void testContestList(ClientDialog cd) throws Exception {
    ClientData.server.doRequest(new CreateContestRequest("Example contest #1"));
    ClientData.server.doRequest(new CreateContestRequest("Example contest #2"));
    ContestDescription[] contestList = ClientData.server
        .doRequest(new AvailableContestsRequest()).contests;
    assertEquals("Example contest #1", contestList[0].name);
    assertEquals("Example contest #2", contestList[1].name);
  }

}
