package ru.ipo.dces.mock;

/**
 * ƒанные о текущей сессии пользовател€. ¬ообще-то хран€тс€ на сервере, но
 * сервер-подделка MockServer хранит их в списке таких структур
 */
public class MockSessionServer {

  public String login;
  public int    contestID;

  public MockSessionServer(String login, int contestID) {
    this.login = login;
    this.contestID = contestID;
  }

}
