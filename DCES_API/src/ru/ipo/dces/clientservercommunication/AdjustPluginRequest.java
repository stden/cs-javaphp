package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: Илья
 * Date: 18.12.2008
 * Time: 23:30:51
 *
 * Запрос на настройку или создание плагина стороны клиента
 */
public class AdjustPluginRequest implements Request {

  /**
   * Идентификатор сессии. Требуется сессия администратора сервера
   */
  @BinInfo(phpDefaultValue="null")
  public String sessionID;

  @BinInfo(phpDefaultValue="'Client'")
  public PluginSide side;

  /**
   * Уникальный идентификатор плагина. Если идентификатор уже зарегистрирован в системе, плагин будет изменен.
   * В противном случае создается новый плагин
   */
  @BinInfo(phpDefaultValue="'Test client plugin'")
  public String pluginAlias;

  /**
   * Содержимое jar файла с плагином. При настройке существующего плагина может быть null, что означает,
   * что содержимое изменять не надо. При создании нового плагина обязательно должно быть не null
   */
  @BinInfo(phpDefaultValue="'jar file content'")
  public byte[] pluginData;

  /**
   * Описание плагина. При настройке существующего плагина может быть null, что означает,
   * что содержимое изменять не надо. При создании нового плагина обязательно должно быть не null
   */
  @BinInfo(phpDefaultValue="'Test client plugin description'")
  public String description;

}
