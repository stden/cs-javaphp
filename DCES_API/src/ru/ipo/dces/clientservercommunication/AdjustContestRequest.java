package ru.ipo.dces.clientservercommunication;

public class AdjustContestRequest implements Request {

  @BinInfo(
          phpDefaultValue="null",
          editable = false
  )
  public String sessionID;

  @BinInfo(
          phpDefaultValue="null",
          defaultValue = BinInfo.NEW_INSTANCE_DEFAULT_VALUE,
          title = "Соревнование"
  )
  public ContestDescription contest;

  @BinInfo(
          phpDefaultValue="null",
          defaultValue = BinInfo.NEW_INSTANCE_DEFAULT_VALUE,
          title = "Задачи"
  )
  public ProblemDescription[] problems;

}
