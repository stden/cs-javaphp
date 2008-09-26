package ru.ipo.dces.mock;

import ru.ipo.dces.clientservercommunication.*;

/**
 * ƒанные о текущей сессии пользовател€. ¬ообще-то хран€тс€ на сервере, но
 * сервер-подделка MockServer хранит их в списке таких структур
 */
public class SessionData {

  public String               login;
  public String               password;
  public ContestDescription   contest;
  public ProblemDescription[] problems;

}
