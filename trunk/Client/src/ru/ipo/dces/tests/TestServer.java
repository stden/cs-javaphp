package ru.ipo.dces.tests;

import org.junit.*;

import ru.ipo.dces.client.MockServer;
import ru.ipo.dces.clientservercommunication.*;

import static org.junit.Assert.*;

/** ��������� ���� ��������� Mock/Real Server'�� */
public class TestServer {

  MockServer server;

  @Before
  public void setUp() throws Exception, RequestFailedResponse {
    // ������ ����� ������-��������
    server = new MockServer();
    // ��������� 2 ��������
    assertNotNull(server.doRequest(AcceptedResponse.class,
        new CreateContestRequest("Contest #1")));
    assertNotNull(server.doRequest(AcceptedResponse.class,
        new CreateContestRequest("Contest #2")));
    // ��������� ������������
    CreateUserRequest cur = new CreateUserRequest("denis", "denispass");
    assertNotNull(server.doRequest(AcceptedResponse.class, cur));
  }

  @Test
  public void testContests() throws Exception, RequestFailedResponse {
    // ����������� ��������� ��������
    AvailableContestsRequest acr = new AvailableContestsRequest();
    acr.getInvisibleContests = false;
    AvailableContestsResponse r = server.doRequest(
        AvailableContestsResponse.class, acr);
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
    ConnectToContestResponse ct = server.doRequest(
        ConnectToContestResponse.class, con);
    // ������������ � �������� �����-�� sessionID
    assertTrue(ct.sessionID != "");
    // � ������ ������ ������������ ����� ��� ������
    con.login = "�������������� ������������";
    con.password = "������";
    // ������ �� ������
    try {
      server.doRequest(ConnectToContestResponse.class, con);
      fail("����� � ������ �������� => ������ ���� RequestFailedResponse");
    } catch (RequestFailedResponse rq) {
      assertEquals("�������� ����� ��� ������", rq.message);
    }
    // �������� ������, ������� ������ � �������

    // ������ ���� ������
    ChangePasswordRequest cpr = new ChangePasswordRequest();
    cpr.sessionID = ct.sessionID;
    cpr.oldPassword = con.password;
    cpr.newPassword = "newdenispass";
    AcceptedResponse ar = server.doRequest(AcceptedResponse.class, cpr);
    assertNotNull(ar);

    // 
  }
}
