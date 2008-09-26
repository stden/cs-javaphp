package ru.ipo.dces.tests;

import org.junit.*;

import ru.ipo.dces.client.IServer;
import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.mock.MockServer;

import static org.junit.Assert.*;

/** Обработка всех сообщений Mock/Real Server'ом */
public class TestServer {

  IServer server;

  /**
   * Отключаемся от контеста
   * 
   * @throws RequestFailedResponse
   * @throws Exception
   */
  private void disconnect(String sessionID) throws RequestFailedResponse,
      Exception {
    // Пробуем отключиться с явно неправильным sessionID
    try {
      server.doRequest(new DisconnectRequest("wrong sessionID"));
      fail("Должно быть исключение: Неверный sessionID");
    } catch (RequestFailedResponse e) {
      assertEquals("Неверный sessionID", e.message);
    }
    // Теперь реально отключаемся - передаём верный sessionID
    assertNotNull(server.doRequest(new DisconnectRequest(sessionID)));
    // Пробуем ещё раз отключиться с тем же sessionID (мы уже отключились от
    // этой сессии)
    try {
      server.doRequest(new DisconnectRequest(sessionID));
      fail("Должно быть исключение: Неверный sessionID");
    } catch (RequestFailedResponse e) {
      assertEquals("Неверный sessionID", e.message);
    }
  }

  /**
   * Удаляем Plugin клиента
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
    // Создаём новый сервер-подделку
    server = new MockServer();
    // Добавляем 2 контеста
    assertNotNull(server.doRequest(new CreateContestRequest("Contest #1")));
    assertNotNull(server.doRequest(new CreateContestRequest("Contest #2")));
    // Добавляем пользователя
    CreateUserRequest cur = new CreateUserRequest("denis", "denispass");
    assertNotNull(server.doRequest(cur));
  }

  @Test
  public void testContests() throws Exception, RequestFailedResponse {
    // Запрашиваем доступные контесты
    AvailableContestsRequest acr = new AvailableContestsRequest();
    acr.getInvisibleContests = false;
    AvailableContestsResponse r = server.doRequest(acr);
    // Получам 2 доступных контеста
    assertEquals(2, r.contests.length);
    assertEquals("Contest #1", r.contests[0].name);
    assertEquals("Contest #2", r.contests[1].name);

    // Хотим присоединиться к первому контесту
    ConnectToContestRequest con = new ConnectToContestRequest();
    // Выбираем его id
    con.contestID = r.contests[0].contestID;
    // Вводим правильные логин и пароль
    con.login = "denis";
    con.password = "denispass";
    // Делаем запрос на сервер
    ConnectToContestResponse curUser = server.doRequest(con);
    // Подключаемся и получаем какой-то sessionID
    assertTrue(curUser.sessionID != "");

    // А теперь вводим неправильные логин или пароль
    con.login = "несуществующий пользователь";
    con.password = "пароль";
    // Запрос на сервер: Логин и пароль неверные => должно быть
    // RequestFailedResponse
    try {
      server.doRequest(con);
      fail("Должно быть исключение: \"" + "Неверный логин или пароль" + "\"");
    } catch (RequestFailedResponse e) {
      assertEquals("Неверный логин или пароль", e.message);
    }
    // Но при этом нас не отсоединяет от контеста

    // Смотрим список участников контеста
    GetUsersRequest gur = new GetUsersRequest();
    gur.sessionID = curUser.sessionID;
    GetUsersResponse rr = server.doRequest(gur);
    // И видим там самого себя
    assertEquals(1, rr.users.length);

    // Получаем данные о контесте #1
    GetContestDataRequest gc = new GetContestDataRequest();
    gc.sessionID = curUser.sessionID;
    GetContestDataResponse gr = server.doRequest(gc);
    assertNotNull(gr);

    // Меняем свой пароль
    ChangePasswordRequest cpr = new ChangePasswordRequest();
    cpr.sessionID = curUser.sessionID;
    cpr.oldPassword = "denispass";
    cpr.newPassword = "newdenispass";
    AcceptedResponse ar = server.doRequest(cpr);
    assertNotNull(ar);

    // А теперь задаём неправильный пароль и снова пытаемся сменить пароль
    ChangePasswordRequest cpr2 = new ChangePasswordRequest();
    cpr.sessionID = curUser.sessionID;
    cpr.oldPassword = "wrongpassword";
    cpr.newPassword = "newdenispass";
    // Должны были получить сообщение об ошибке, ибо пароль неправильный
    try {
      server.doRequest(cpr2);
      fail("Должно быть исключение: \"" + "Неверный пароль" + "\"");
    } catch (RequestFailedResponse e) {
      assertEquals("Неверный пароль", e.message);
    }

    // Настроим текущий контест
    AdjustContestRequest ad = new AdjustContestRequest();
    ad.sessionID = curUser.sessionID;
    // Например, зададим ему новое имя
    ad.contest = new ContestDescription("Contest #1!");
    ad.contest.contestID = 1;
    assertNotNull(server.doRequest(ad));

    // Получаем задачи, которые входят в контест

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
