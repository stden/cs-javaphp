package ru.ipo.dces.clientservercommunication;

import ru.ipo.dces.clientservercommunication.ContestDescription;

/**
 * �������� ������ ��������
 */
public class CreateContestRequest implements Request {

  public String             sessionID;
  public ContestDescription contest;

}
