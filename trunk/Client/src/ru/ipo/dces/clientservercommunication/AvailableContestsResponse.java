package ru.ipo.dces.clientservercommunication;

import ru.ipo.dces.clientservercommunication.ContestDescription;

/**
 * Ответ - список всех контестов
 */
public class AvailableContestsResponse implements Response {

  public ContestDescription[] contests;

}
