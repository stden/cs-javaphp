package ru.ipo.dces.clientservercommunication;

import java.util.Date;

/**
 * �����: �������������� � ������������
 */
public class ConnectToContestResponse implements Response {

  public String          sessionID;

  /** �������� ������������ */
  public UserDescription user;

  public Date finishTime; 
}
