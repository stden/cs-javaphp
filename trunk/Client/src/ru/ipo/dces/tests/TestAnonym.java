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

  /** Отправляем все виды сообщений и проверяем реакцию сервера */
  @Test
  public void testAnonymUser() throws Exception {
    server = new MockServer();

    try {
      server.doRequest(new AdjustContestRequest());
      fail(msg_Expected_wrong_SessionID);
    } catch (Exception e) {
      assertEquals("Неверный sessionID", e.getMessage());
    }

    assertNotNull(server.doRequest(new AvailableContestsRequest()));

    try {
      server.doRequest(new ChangePasswordRequest());
      fail(msg_Expected_wrong_SessionID);
    } catch (Exception e) {
      assertEquals("Неверный sessionID", e.getMessage());
    }

    try {
      server.doRequest(new ConnectToContestRequest());
      fail(msg_Expected_wrong_SessionID);
    } catch (Exception e) {
      assertEquals("Неверный логин или пароль", e.getMessage());
    }

    try {
      server.doRequest(new CreateContestRequest("Contest #1"));
      fail(msg_Expected_wrong_SessionID);
    } catch (Exception e) {
      assertEquals("Неверный sessionID", e.getMessage());
    }

    try {
      server.doRequest(new CreateUserRequest("login", "password"));
      fail(msg_Expected_wrong_SessionID);
    } catch (Exception e) {
      assertEquals("Неверный sessionID", e.getMessage());
    }

    try {
      server.doRequest(new DisconnectRequest("wrong sessionID"));
      fail(msg_Expected_wrong_SessionID);
    } catch (Exception e) {
      assertEquals("Неверный sessionID", e.getMessage());
    }

    try {
      server.doRequest(new GetContestDataRequest());
      fail(msg_Expected_wrong_SessionID);
    } catch (Exception e) {
      assertEquals("Неверный sessionID", e.getMessage());
    }

    try {
      server.doRequest(new GetUsersRequest());
      fail(msg_Expected_wrong_SessionID);
    } catch (Exception e) {
      assertEquals("Неверный sessionID", e.getMessage());
    }

    try {
      server.doRequest(new InstallClientPluginRequest());
      fail(msg_Expected_wrong_SessionID);
    } catch (Exception e) {
      assertEquals("Неверный sessionID", e.getMessage());
    }

    // Делать запрос на восстановление пароля можно всем
    assertNotNull(server.doRequest(new RestorePasswordRequest()));

    try {
      server.doRequest(new SubmitSolutionRequest());
      fail(msg_Expected_wrong_SessionID);
    } catch (Exception e) {
      assertEquals("Неверный sessionID", e.getMessage());
    }

  }

  /** Отправляем невозможные типы сообщений */

}
