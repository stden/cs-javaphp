package ru.ipo.dces.tests;

import org.junit.*;

import ru.ipo.dces.client.MockServer;
import ru.ipo.dces.clientservercommunication.*;

import static org.junit.Assert.*;

/** Обработка всех сообщений Mock/Real Server'ом */
public class TestServer {

  MockServer server;

  @Before
  public void setUp() throws Exception {
    // Создаём новый сервер-подделку
    server = new MockServer();
    // Добавляем два контеста для примера
    server.addContest("Contest #1");
    server.addContest("Contest #2");
    // Добавляем пользователя
    server.addUser("denis", "denispass");
  }

  @Test
  public void testContests() throws Exception, RequestFailedResponse {
    // Запрашиваем доступные контесты
    AvailableContestsRequest acr = new AvailableContestsRequest();
    acr.getInvisibleContests = false;
    AvailableContestsResponse r = server.doRequest(
        AvailableContestsResponse.class, acr);
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
    ConnectToContestResponse ct = server.doRequest(
        ConnectToContestResponse.class, con);
    // Подключаемся и получаем какой-то sessionID
    assertTrue(ct.sessionID != "");
    // А теперь вводим неправильные логин или пароль
    con.login = "несуществующий пользователь";
    con.password = "пароль";
    // Запрос на сервер
    try {
      server.doRequest(ConnectToContestResponse.class, con);
      fail("Логин и пароль неверные => должно быть RequestFailedResponse");
    } catch (RequestFailedResponse rq) {
      assertEquals("Неверный логин или пароль", rq.message);
    }
    // Получаем задачи, которые входят в контест

    // Меняем свой пароль
    ChangePasswordRequest cpr = new ChangePasswordRequest();
    cpr.sessionID = ct.sessionID;
    cpr.oldPassword = con.password;
    cpr.newPassword = "newdenispass";
    server.doRequest(ChangePasswordRequest.class, cpr);
  }
}
