//package ru.ipo.dces.tests;
//
//import org.junit.Test;
//
//import ru.ipo.dces.client.Controller;
//import ru.ipo.dces.clientservercommunication.*;
//import ru.ipo.dces.mock.MockServer;
//
//import static org.junit.Assert.*;
//
///** ��������� ���� ��������� Mock/Real Server'�� */
//public class TestServer {
//
//  MockServer                  server;
//
//  private static final String msg_Expected_wrong_SessionID = "������ ���� ����������: �������� sessionID";
//
//  /**
//   * ����������� �� ��������
//   *
//   * @param sessionID is a id of a session
//   * @throws Exception if error occurs
//   */
//  private void disconnect(String sessionID) throws Exception {
//    // ������� ����������� � ���� ������������ sessionID
//    try {
//      server.doRequest(new DisconnectRequest("wrong sessionID"));
//      fail(msg_Expected_wrong_SessionID);
//    } catch (Exception e) {
//      assertEquals("�������� sessionID", e.getMessage());
//    }
//    // ������ ������� ����������� - ������� ������ sessionID
//    assertNotNull(server.doRequest(new DisconnectRequest(sessionID)));
//    // ������� ��� ��� ����������� � ��� �� sessionID (�� ��� ����������� ��
//    // ���� ������)
//    try {
//      server.doRequest(new DisconnectRequest(sessionID));
//      fail(msg_Expected_wrong_SessionID);
//    } catch (Exception e) {
//      assertEquals("�������� sessionID", e.getMessage());
//    }
//  }
//
//  /**
//   * ������� Plugin �������
//   *
//   * @throws Exception if err
//   */
//  private void removePlugin() throws Exception {
//    AcceptedResponse ar12 = server.doRequest(new RemovePluginRequest());
//    assertNotNull(ar12);
//  }
//
//  private void restorePassword() throws Exception {
//    assertNotNull(server.doRequest(new RestorePasswordRequest()));
//  }
//
//  @Test
//  public void testContests() throws Exception {
//    // ������ ����� ������-��������
//    server = new MockServer();
//
//    // ��������� ��� �������������
//    ConnectToContestRequest cc = new ConnectToContestRequest();
//    cc.contestID = 0;
//    cc.login = "admin";
//    cc.password = "adminpass";
//    Controller.sessionID = server.doRequest(cc).sessionID;
//
//    // ��������� 2 ��������
//    assertNotNull(server.doRequest(new CreateContestRequest("Contest #1")));
//    assertNotNull(server.doRequest(new CreateContestRequest("Contest #2")));
//    // � ���� ���������
//    CreateContestRequest invCont = new CreateContestRequest("Contest Invisible");
//    //invCont.contest.visible = false;
//    assertNotNull(server.doRequest(invCont));
//    // ��������� ������������
//    RegisterToContestRequest cur = new RegisterToContestRequest();
//    cur.sessionID = Controller.sessionID;
//    cur.contestID = 1; //TODO here is to be the id of just created contest
//    cur.user = new UserDescription();
//    cur.user.userType = UserDescription.UserType.Participant;
//    cur.user.login = "denis";
//    cur.user.password = "denispass";
//    assertNotNull(server.doRequest(cur));
//
//    // ����������� ��������� ������� ��������
//    AvailableContestsRequest acr = new AvailableContestsRequest();
//    AvailableContestsResponse r = server.doRequest(acr);
//    // ������� 2 ��������� ��������
//    assertEquals(2, r.contests.length);
//    assertEquals("Contest #1", r.contests[0].name);
//    assertEquals("Contest #2", r.contests[1].name);
//
//    // ����� �������������� � ������� ��������
//    ConnectToContestRequest con = new ConnectToContestRequest();
//    // �������� ��� id
//    con.contestID = r.contests[0].contestID;
//    // ������ ���������� ����� � ������
//    con.login = "denis";
//    con.password = "denispass";
//    // ������ ������ �� ������
//    ConnectToContestResponse curUser = server.doRequest(con);
//    // ������������ � �������� �����-�� sessionID
//    assertTrue(!curUser.sessionID.equals(""));
//
//    // � ������ ������ ������������ ����� ��� ������
//    con.login = "�������������� ������������";
//    con.password = "������";
//    // ������ �� ������: ����� � ������ �������� => ������ ����
//    // RequestFailedResponse
//    try {
//      server.doRequest(con);
//      fail("������ ���� ����������: \"" + "�������� ����� ��� ������" + "\"");
//    } catch (Exception e) {
//      assertEquals("�������� ����� ��� ������", e.getMessage());
//    }
//    // �� ��� ���� ��� �� ����������� �� ��������
//
//    // ������� ������ ���������� ��������
//    GetUsersRequest gur = new GetUsersRequest();
//    gur.sessionID = curUser.sessionID;
//    GetUsersResponse rr = server.doRequest(gur);
//    // � ����� ��� ������ ����
//    assertEquals(1, rr.users.length);
//    assertEquals("denis", rr.users[0].login);
//
//    // �������� ������ � �������� #1
//    GetContestDataRequest gc = new GetContestDataRequest();
//    gc.sessionID = curUser.sessionID;
//    GetContestDataResponse gr = server.doRequest(gc);
//    assertNotNull(gr);
//
//    // ������ ���� ������
//    ChangePasswordRequest cpr = new ChangePasswordRequest();
//    cpr.sessionID = curUser.sessionID;
//    cpr.oldPassword = "denispass";
//    cpr.newPassword = "newdenispass";
//    AcceptedResponse ar = server.doRequest(cpr);
//    assertNotNull(ar);
//
//    // � ������ ����� ������������ ������ � ����� �������� ������� ������
//    cpr = new ChangePasswordRequest();
//    cpr.sessionID = curUser.sessionID;
//    cpr.oldPassword = "wrongpassword";
//    cpr.newPassword = "newdenispass";
//    // ������ ���� �������� ��������� �� ������, ��� ������ ������������
//    try {
//      server.doRequest(cpr);
//      fail("������ ���� ����������: \"" + "�������� ������" + "\"");
//    } catch (Exception e) {
//      assertEquals("�������� ������", e.getMessage());
//    }
//
//    // �������� ������� �������
//    AdjustContestRequest ad = new AdjustContestRequest();
//    ad.sessionID = curUser.sessionID;
//    // ��������, ������� ��� ����� ���
//    ad.contest = new ContestDescription("Contest #1!");
//    ad.contest.contestID = 1;
//    // ������� 8 �����
//    ad.problems = new ProblemDescription[8];
//    for (int i = 0; i < ad.problems.length; i++) {
//      ad.problems[i] = new ProblemDescription();
//      ad.problems[i].id = i;
//      ad.problems[i].name = "problem " + (char) ('A' + i);
//    }
//    assertNotNull(server.doRequest(ad));
//
//    // �������� ������, ������� ������ � �������
//    GetContestDataRequest c2 = new GetContestDataRequest();
//    c2.sessionID = curUser.sessionID;
//    GetContestDataResponse cdd = server.doRequest(c2);
//    assertEquals("Contest #1", cdd.contest.name);
//    assertEquals("problem A", cdd.problems[0].name);
//
//    DownloadPluginRequest r2 = new DownloadPluginRequest();
//    //r2.sessionID = curUser.sessionID;
//    DownloadPluginResponse dd = server.doRequest(r2);
//    assertNotNull(dd);
//
//    AcceptedResponse ar11 = server.doRequest(new RegisterToContestRequest());
//    assertNotNull(ar11);
//
//    restorePassword();
//
//    removePlugin();
//
//    assertNotNull(server.doRequest(new UploadClientPluginRequest()));
//
//    disconnect(curUser.sessionID);
//
//  }
//}
