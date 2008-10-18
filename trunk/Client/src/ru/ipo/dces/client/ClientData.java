package ru.ipo.dces.client;

import ru.ipo.dces.clientservercommunication.*;

/** Данные о соединении с сервером */
public class ClientData {
  public static ServerFacade       server;
  public static String             sessionID;
  public static UserDescription    curUser;
  public static ContestDescription curContest;
}
