package ru.ipo.dces.tests;

import org.junit.*;

import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.mock.MockServer;

import static org.junit.Assert.*;

/** ������ ���������� ������������ */
public class TestAnonym {

  MockServer                  server;

  private static final String msg_Expected_wrong_SessionID = "������ ���� ����������: �������� sessionID";

  @Before
  public void setUp() throws Exception {
    server = new MockServer();
  }

  /** ���������� ��� ���� ��������� � ������� ������� ������� */
  @Test
  public void testAnonymUser() throws Exception, RequestFailedResponse {
    server = new MockServer();

    // ������� ��������� �������
    try {
      server.doRequest(new AdjustContestRequest());
      fail(msg_Expected_wrong_SessionID);
    } catch (RequestFailedResponse e) {
      assertEquals("�������� sessionID", e.message);
    }

    assertNotNull(server.doRequest(new AvailableContestsRequest()));

    // ������� ������� ������
    try {
      server.doRequest(new ChangePasswordRequest());
      fail(msg_Expected_wrong_SessionID);
    } catch (RequestFailedResponse e) {
      assertEquals("�������� sessionID", e.message);
    }

    // ������� ������� ������
    try {
      server.doRequest(new ConnectToContestRequest());
      fail(msg_Expected_wrong_SessionID);
    } catch (RequestFailedResponse e) {
      assertEquals("�������� ����� ��� ������", e.message);
    }

    // ������� �������� ������� ���������������
    try {
      server.doRequest(new CreateContestRequest("Contest #1"));
      fail(msg_Expected_wrong_SessionID);
    } catch (RequestFailedResponse e) {
      assertEquals("�������� sessionID", e.message);
    }

    // ������� ������� ������������
    try {
      server.doRequest(new CreateUserRequest("login", "password"));
      fail(msg_Expected_wrong_SessionID);
    } catch (RequestFailedResponse e) {
      assertEquals("�������� sessionID", e.message);
    }

    // ������� ������� ������������
    try {
      server.doRequest(new DisconnectRequest("wrong sessionID"));
      fail(msg_Expected_wrong_SessionID);
    } catch (RequestFailedResponse e) {
      assertEquals("�������� sessionID", e.message);
    }

  }

  /** ���������� ����������� ���� ��������� */

}
