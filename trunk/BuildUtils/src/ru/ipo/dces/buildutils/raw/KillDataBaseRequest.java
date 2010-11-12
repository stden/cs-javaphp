package ru.ipo.dces.buildutils.raw;

/**
 * Created by IntelliJ IDEA.
 * User: Ilya
 * Date: 13.11.2009
 * Time: 0:00:57
 *
 * Удивание базы со всей структурой. Чаще всего будет использоваться при тестировании
 */
public class KillDataBaseRequest {

  @BinInfo(phpDefaultValue="null")
  public String sessionID;

}
