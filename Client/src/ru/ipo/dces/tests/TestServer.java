package ru.ipo.dces.tests;

import org.junit.*;

import ru.ipo.dces.client.IServer;
import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.mock.MockServer;

import static org.junit.Assert.*;

/** ��������� ���� ��������� Mock/Real Server'�� */
public class TestServer {

  IServer server;

  /**
   * ����������� �� ��������
   * 
   * @throws RequestFailedResponse
   * @throws Exception
   */
  private void disconnect(String sessionID) throws RequestFailedResponse,
      Exception {
    // ������� ����������� � ���� ������������ sessionID
    try {
      server.doRequest(new DisconnectRequest("wrong sessionID"));
      fail("������ ���� ����������: �������� sessionID");
    } catch (RequestFailedResponse e) {
      assertEquals("�������� sessionID", e.message);
    }
    // ������ ������� ����������� - ������� ������ sessionID
    assertNotNull(server.doRequest(new DisconnectRequest(sessionID)));
    // ������� ��� ��� ����������� � ��� �� sessionID (�� ��� ����������� ��
    // ���� ������)
    try {
      server.doRequest(new DisconnectRequest(sessionID));
      fail("������ ���� ����������: �������� sessionID");
    } catch (RequestFailedResponse e) {
      assertEquals("�������� sessionID", e.message);
    }
  }

  /**
   * ������� Plugin �������
   * 
   * @throws Exception
   */
  private void removePlugin() throws Exception {
    AcceptedResponse ar12 = server.doRequest(new RemoveClientPluginRequest());
    assertNotNull(ar12);
  }

  private void restorePassword() throws Exception {
    assertNotNull(server.doRequest(new RestorePasswordRequest()));
  }

  @Before
  public void setUp() throws Exception, RequestFailedResponse {
    // ������ ����� ������-��������
    server = new MockServer();
    // ��������� 2 ��������
    assertNotNull(server.doRequest(new CreateContestRequest("Contest #1")));
    assertNotNull(server.doRequest(new CreateContestRequest("Contest #2")));
    // ��������� ������������
    CreateUserRequest cur = new CreateUserRequest("denis", "denispass");
    assertNotNull(server.doRequest(cur));
  }

  @Test
  public void testContests() throws Exception, RequestFailedResponse {
    // ����������� ��������� ��������
    AvailableContestsRequest acr = new AvailableContestsRequest();
    acr.getInvisibleContests = false;
    AvailableContestsResponse r = server.doRequest(acr);
    // ������� 2 ��������� ��������
    assertEquals(2, r.contests.length);
    assertEquals("Contest #1", r.contests[0].name);
    assertEquals("Contest #2", r.contests[1].name);

    // ����� �������������� � ������� ��������
    ConnectToContestRequest con = new ConnectToContestRequest();
    // �������� ��� id
    con.contestID = r.contests[0].contestID;
    // ������ ���������� ����� � ������
    con.login = "denis";
    con.password = "denispass";
    // ������ ������ �� ������
    ConnectToContestResponse curUser = server.doRequest(con);
    // ������������ � �������� �����-�� sessionID
    assertTrue(curUser.sessionID != "");

    // � ������ ������ ������������ ����� ��� ������
    con.login = "�������������� ������������";
    con.password = "������";
    // ������ �� ������: ����� � ������ �������� => ������ ����
    // RequestFailedResponse
    try {
      server.doRequest(con);
      fail("������ ���� ����������: \"" + "�������� ����� ��� ������" + "\"");
    } catch (RequestFailedResponse e) {
      assertEquals("�������� ����� ��� ������", e.message);
    }
    // �� ��� ���� ��� �� ����������� �� ��������

    // ������� ������ ���������� ��������
    GetUsersRequest gur = new GetUsersRequest();
    gur.sessionID = curUser.sessionID;
    GetUsersResponse rr = server.doRequest(gur);
    // � ����� ��� ������ ����
    assertEquals(1, rr.users.length);

    // �������� ������ � �������� #1
    GetContestDataRequest gc = new GetContestDataRequest();
    gc.sessionID = curUser.sessionID;
    GetContestDataResponse gr = server.doRequest(gc);
    assertNotNull(gr);

    // ������ ���� ������
    ChangePasswordRequest cpr = new ChangePasswordRequest();
    cpr.sessionID = curUser.sessionID;
    cpr.oldPassword = "denispass";
    cpr.newPassword = "newdenispass";
    AcceptedResponse ar = server.doRequest(cpr);
    assertNotNull(ar);

    // � ������ ����� ������������ ������ � ����� �������� ������� ������
    ChangePasswordRequest cpr2 = new ChangePasswordRequest();
    cpr.sessionID = curUser.sessionID;
    cpr.oldPassword = "wrongpassword";
    cpr.newPassword = "newdenispass";
    // ������ ���� �������� ��������� �� ������, ��� ������ ������������
    try {
      server.doRequest(cpr2);
      fail("������ ���� ����������: \"" + "�������� ������" + "\"");
    } catch (RequestFailedResponse e) {
      assertEquals("�������� ������", e.message);
    }

    // �������� ������� �������
    AdjustContestRequest ad = new AdjustContestRequest();
    ad.sessionID = curUser.sessionID;
    // ��������, ������� ��� ����� ���
    ad.contest = new ContestDescription("Contest #1!");
    ad.contest.contestID = 1;
    assertNotNull(server.doRequest(ad));

    // �������� ������, ������� ������ � �������

    InstallClientPluginResponse dd = server
        .doRequest(new InstallClientPluginRequest());
    assertNotNull(dd);

    AcceptedResponse ar11 = server.doRequest(new RegisterToContestRequest());
    assertNotNull(ar11);

    restorePassword();

    removePlugin();

    disconnect(curUser.sessionID);

  }
}
