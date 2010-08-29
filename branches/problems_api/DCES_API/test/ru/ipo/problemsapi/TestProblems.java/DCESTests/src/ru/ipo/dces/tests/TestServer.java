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
///** Обработка всех сообщений Mock/Real Server'ом */
//public class TestServer {
//
//  MockServer                  server;
//
//  private static final String msg_Expected_wrong_SessionID = "Должно быть исключение: Неверный sessionID";
//
//  /**
//   * Отключаемся от контеста
//   *
//   * @param sessionID is a id of a session
//   * @throws Exception if error occurs
//   */
//  private void disconnect(String sessionID) throws Exception {
//    // Пробуем отключиться с явно неправильным sessionID
//    try {
//      server.doRequest(new DisconnectRequest("wrong sessionID"));
//      fail(msg_Expected_wrong_SessionID);
//    } catch (Exception e) {
//      assertEquals("Неверный sessionID", e.getMessage());
//    }
//    // Теперь реально отключаемся - передаём верный sessionID
//    assertNotNull(server.doRequest(new DisconnectRequest(sessionID)));
//    // Пробуем ещё раз отключиться с тем же sessionID (мы уже отключились от
//    // этой сессии)
//    try {
//      server.doRequest(new DisconnectRequest(sessionID));
//      fail(msg_Expected_wrong_SessionID);
//    } catch (Exception e) {
//      assertEquals("Неверный sessionID", e.getMessage());
//    }
//  }
//
//  /**
//   * Удаляем Plugin клиента
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
//    // Создаём новый сервер-подделку
//    server = new MockServer();
//
//    // Логинимся как администратор
//    ConnectToContestRequest cc = new ConnectToContestRequest();
//    cc.contestID = 0;
//    cc.login = "admin";
//    cc.password = "adminpass";
//    Controller.sessionID = server.doRequest(cc).sessionID;
//
//    // Добавляем 2 контеста
//    assertNotNull(server.doRequest(new CreateContestRequest("Contest #1")));
//    assertNotNull(server.doRequest(new CreateContestRequest("Contest #2")));
//    // И один невидимый
//    CreateContestRequest invCont = new CreateContestRequest("Contest Invisible");
//    //invCont.contest.visible = false;
//    assertNotNull(server.doRequest(invCont));
//    // Добавляем пользователя
//    RegisterToContestRequest cur = new RegisterToContestRequest();
//    cur.sessionID = Controller.sessionID;
//    cur.contestID = 1; //TODO here is to be the id of just created contest
//    cur.user = new UserDescription();
//    cur.user.userType = UserDescription.UserType.Participant;
//    cur.user.login = "denis";
//    cur.user.password = "denispass";
//    assertNotNull(server.doRequest(cur));
//
//    // Запрашиваем доступные видимые контесты
//    AvailableContestsRequest acr = new AvailableContestsRequest();
//    AvailableContestsResponse r = server.doRequest(acr);
//    // Получам 2 доступных контеста
//    assertEquals(2, r.contests.length);
//    assertEquals("Contest #1", r.contests[0].name);
//    assertEquals("Contest #2", r.contests[1].name);
//
//    // Хотим присоединиться к первому контесту
//    ConnectToContestRequest con = new ConnectToContestRequest();
//    // Выбираем его id
//    con.contestID = r.contests[0].contestID;
//    // Вводим правильные логин и пароль
//    con.login = "denis";
//    con.password = "denispass";
//    // Делаем запрос на сервер
//    ConnectToContestResponse curUser = server.doRequest(con);
//    // Подключаемся и получаем какой-то sessionID
//    assertTrue(!curUser.sessionID.equals(""));
//
//    // А теперь вводим неправильные логин или пароль
//    con.login = "несуществующий пользователь";
//    con.password = "пароль";
//    // Запрос на сервер: Логин и пароль неверные => должно быть
//    // RequestFailedResponse
//    try {
//      server.doRequest(con);
//      fail("Должно быть исключение: \"" + "Неверный логин или пароль" + "\"");
//    } catch (Exception e) {
//      assertEquals("Неверный логин или пароль", e.getMessage());
//    }
//    // Но при этом нас не отсоединяет от контеста
//
//    // Смотрим список участников контеста
//    GetUsersRequest gur = new GetUsersRequest();
//    gur.sessionID = curUser.sessionID;
//    GetUsersResponse rr = server.doRequest(gur);
//    // И видим там самого себя
//    assertEquals(1, rr.users.length);
//    assertEquals("denis", rr.users[0].login);
//
//    // Получаем данные о контесте #1
//    GetContestDataRequest gc = new GetContestDataRequest();
//    gc.sessionID = curUser.sessionID;
//    GetContestDataResponse gr = server.doRequest(gc);
//    assertNotNull(gr);
//
//    // Меняем свой пароль
//    ChangePasswordRequest cpr = new ChangePasswordRequest();
//    cpr.sessionID = curUser.sessionID;
//    cpr.oldPassword = "denispass";
//    cpr.newPassword = "newdenispass";
//    AcceptedResponse ar = server.doRequest(cpr);
//    assertNotNull(ar);
//
//    // А теперь задаём неправильный пароль и снова пытаемся сменить пароль
//    cpr = new ChangePasswordRequest();
//    cpr.sessionID = curUser.sessionID;
//    cpr.oldPassword = "wrongpassword";
//    cpr.newPassword = "newdenispass";
//    // Должны были получить сообщение об ошибке, ибо пароль неправильный
//    try {
//      server.doRequest(cpr);
//      fail("Должно быть исключение: \"" + "Неверный пароль" + "\"");
//    } catch (Exception e) {
//      assertEquals("Неверный пароль", e.getMessage());
//    }
//
//    // Настроим текущий контест
//    AdjustContestRequest ad = new AdjustContestRequest();
//    ad.sessionID = curUser.sessionID;
//    // Например, зададим ему новое имя
//    ad.contest = new ContestDescription("Contest #1!");
//    ad.contest.contestID = 1;
//    // Добавим 8 задач
//    ad.problems = new ProblemDescription[8];
//    for (int i = 0; i < ad.problems.length; i++) {
//      ad.problems[i] = new ProblemDescription();
//      ad.problems[i].id = i;
//      ad.problems[i].name = "problem " + (char) ('A' + i);
//    }
//    assertNotNull(server.doRequest(ad));
//
//    // Получаем задачи, которые входят в контест
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
