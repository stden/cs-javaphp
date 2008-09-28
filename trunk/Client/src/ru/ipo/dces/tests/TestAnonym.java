package ru.ipo.dces.tests;

import org.junit.*;

import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.mock.MockServer;

import static org.junit.Assert.*;

/** Работа анонимного пользователя */
public class TestAnonym {

  MockServer                  server;

  private static final String msg_Expected_wrong_SessionID = "Должно быть исключение: Неверный sessionID";

  @Before
  public void setUp() throws Exception {
    server = new MockServer();
  }

  /** Отправляем все виды сообщений и смотрим реакцию сервера */
  @Test
  public void testAnonymUser() throws Exception, RequestFailedResponse {
    server = new MockServer();

    // Пробуем настроить контест
    try {
      server.doRequest(new AdjustContestRequest());
      fail(msg_Expected_wrong_SessionID);
    } catch (RequestFailedResponse e) {
      assertEquals("Неверный sessionID", e.message);
    }

    assertNotNull(server.doRequest(new AvailableContestsRequest()));

    // Пробуем сменить пароль
    try {
      server.doRequest(new ChangePasswordRequest());
      fail(msg_Expected_wrong_SessionID);
    } catch (RequestFailedResponse e) {
      assertEquals("Неверный sessionID", e.message);
    }

    // Пробуем сменить пароль
    try {
      server.doRequest(new ConnectToContestRequest());
      fail(msg_Expected_wrong_SessionID);
    } catch (RequestFailedResponse e) {
      assertEquals("Неверный логин или пароль", e.message);
    }

    // Пробуем добавить контест незалогинившись
    try {
      server.doRequest(new CreateContestRequest("Contest #1"));
      fail(msg_Expected_wrong_SessionID);
    } catch (RequestFailedResponse e) {
      assertEquals("Неверный sessionID", e.message);
    }

    // Пробуем создать пользователя
    try {
      server.doRequest(new CreateUserRequest("login", "password"));
      fail(msg_Expected_wrong_SessionID);
    } catch (RequestFailedResponse e) {
      assertEquals("Неверный sessionID", e.message);
    }

    // Пробуем создать пользователя
    try {
      server.doRequest(new DisconnectRequest("wrong sessionID"));
      fail(msg_Expected_wrong_SessionID);
    } catch (RequestFailedResponse e) {
      assertEquals("Неверный sessionID", e.message);
    }

  }

  /** Отправляем невозможные типы сообщений */

}
