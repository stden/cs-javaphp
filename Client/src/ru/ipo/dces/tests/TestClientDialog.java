package ru.ipo.dces.tests;

import java.awt.Component;

import javax.swing.*;

import org.junit.*;

import ru.ipo.dces.client.*;
import ru.ipo.dces.client.ClientDialog.OpenPanelAction;
import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.mock.MockServer;

import static org.junit.Assert.*;

public class TestClientDialog {

  ClientDialog cd;

  /**
   * ����� �������� ������� ����� ������ MockServer � ������� ���� ������� ���
   * ������������
   * 
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception {
    ClientData.server = new MockServer();
    cd = new ClientDialog();

    // ��������� ��� �������������
    ConnectToContestRequest cc = new ConnectToContestRequest();
    cc.contestID = -1;
    cc.login = "admin";
    cc.password = "adminpass";
    ClientData.sessionID = ClientData.server.doRequest(cc).sessionID;

    // ��������� 2 ��������
    assertNotNull(ClientData.server.doRequest(new CreateContestRequest(
        "Contest #1")));
    assertNotNull(ClientData.server.doRequest(new CreateContestRequest(
        "Contest #2")));
  }

  /**
   * �������� �������� ��������������� � �������� ��� ��������� ���������
   * ���������. �� ������ ������ ������ ��������� ���������. ������� ������
   * ������.
   */
  @Test
  public void test1() throws Exception, RequestFailedResponse {
    ContestDescription[] contestList = ClientData.server
        .doRequest(new AvailableContestsRequest()).contests;
    assertEquals("Contest #1", contestList[0].name);
    assertEquals("Contest #2", contestList[1].name);
    // ������ ���������� �� ������
    // cd.adminPanel.reloadButton.getActionListeners()[0].actionPerformed(null);
    // ���������, ��� �������� � ���������
    // cd.adminPanel.contestList.setSelectedIndex(0);
    // ContestDescription cc = (ContestDescription) cd.adminPanel.contestList
    // .getSelectedValue();
    // assertEquals("Contest #1", cc.name);
  }

  /** ��������� ������������ ����� ���������� �������. */
  @Test
  public void test2() throws Exception, RequestFailedResponse {
    AvailableContestsResponse acr = ClientData.server
        .doRequest(new AvailableContestsRequest());
    ContestDescription contest = acr.contests[0];
    assertEquals("Contest #1", contest.name);
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

  /** ������������ ������� � ������ �� ����� ������ */
  @Test
  public void testPanels() {
    // ��� ������ ������� ���������� ���������� ����
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

}
