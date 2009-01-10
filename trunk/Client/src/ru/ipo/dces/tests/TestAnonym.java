package ru.ipo.dces.tests;

import org.junit.*;

import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.client.RealServer;
import ru.ipo.dces.client.Settings;

import static org.junit.Assert.*;

/** Работа анонимного пользователя */
public class TestAnonym {

  RealServer                  server;

  private static final String msg_Expected_wrong_SessionID = "Должно быть исключение: Неверный sessionID";

  @Before
  public void setUp() throws Exception {
    server = new RealServer(Settings.getInstance().getHost());
  }

  /** Отправляем все виды сообщений и проверяем реакцию сервера */
  @Test
  public void testAnonymUser() {
    server = new RealServer(Settings.getInstance().getHost());

    try {
      server.doRequest(new AdjustContestRequest());
      fail(msg_Expected_wrong_SessionID);
    } catch (Exception e) {
      //do nothing
    }

    try {
      server.doRequest(new AvailableContestsRequest());
    } catch (Exception e) {
      //do nothing
    }

    /* //TODO to implement change password request
    try {
      server.doRequest(new ChangePasswordRequest());
      fail(msg_Expected_wrong_SessionID);
    } catch (Exception e) {
      //do nothing
    }
    */

    try {
      server.doRequest(new ConnectToContestRequest());
      fail(msg_Expected_wrong_SessionID);
    } catch (Exception e) {
      //do nothing
    }

    try {
      final CreateContestRequest contestRequest = new CreateContestRequest();
      contestRequest.sessionID = "42";
      server.doRequest(contestRequest);
      fail(msg_Expected_wrong_SessionID);
    } catch (Exception e) {
      //do nothing
    }

    try {
      final CreateUserRequest userRequest = new CreateUserRequest();
      userRequest.user.login = "login";
      userRequest.user.password = "password";
      server.doRequest(userRequest);
      fail(msg_Expected_wrong_SessionID);
    } catch (Exception e) {
      //do nothing
    }

    try {
      final DisconnectRequest r = new DisconnectRequest();
      r.sessionID = "42";
      server.doRequest(r);
      fail(msg_Expected_wrong_SessionID);
    } catch (Exception e) {
      //do nothing
    }

    try {
      server.doRequest(new GetContestDataRequest());
      fail(msg_Expected_wrong_SessionID);
    } catch (Exception e) {
      //do nothing
    }

    try {
      server.doRequest(new GetUsersRequest());
      fail(msg_Expected_wrong_SessionID);
    } catch (Exception e) {
      //do nothing
    }

    try {
      server.doRequest(new InstallClientPluginRequest());
      fail(msg_Expected_wrong_SessionID);
    } catch (Exception e) {
      //do nothing
    }

    /* //TODO implement this request
    // Делать запрос на восстановление пароля можно всем
    try {
      assertNotNull(server.doRequest(new RestorePasswordRequest()));
    } catch (Exception e) {
      //do nothing
    }*/

    try {
      server.doRequest(new SubmitSolutionRequest());
      fail(msg_Expected_wrong_SessionID);
    } catch (Exception e) {
      //do nothing
    }

  }

  /** Отправляем невозможные типы сообщений */

}
