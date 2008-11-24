package ru.ipo.dces.clientservercommunication;

/**
 * «арегистрироватьс€ дл€ участи€ в контесте
 */
public class RegisterToContestRequest implements Request {
  public String sessionID; //leave it null if this request is anonymous
  public int contestID;
  public UserDescription user;
}
