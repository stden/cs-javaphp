package ru.ipo.dces.clientservercommunication;

/**
 * «арегистрироватьс€ дл€ участи€ в контесте
 */
public class RegisterToContestRequest implements Request {
  public String          contestID;
  public UserDescription user;
}
