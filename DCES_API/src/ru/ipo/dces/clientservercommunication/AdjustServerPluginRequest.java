package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 04.05.2009
 * Time: 1:38:00
 *
 * Запрос на настройку или создание плагина стороны сервера
 */
public class AdjustServerPluginRequest implements Request {

  /**
   * Идентификатор сессии. Требуется сессия администратора сервера
   */
  @PHPDefaultValue("null")
  public String sessionID;

  /**
   * Уникальный идентификатор плагина. Если идентификатор уже зарегистрирован в системе, плагин будет изменен.
   * В противном случае создается новый плагин
   */
  @PHPDefaultValue("'Test server plugin'")
  public String pluginAlias;

  /**
   * Содержимое php файла с плагином. При настройке существующего плагина может быть null, что означает,
   * что содержимое изменять не надо. При создании нового плагина обязательно должно быть не null
   */
  @PHPDefaultValue("'jar file content'")
  public byte[] pluginData;

  /**
   * Описание плагина. При настройке существующего плагина может быть null, что означает,
   * что содержимое изменять не надо. При создании нового плагина обязательно должно быть не null
   */
  @PHPDefaultValue("'Test server plugin description'")
  public String description;

}

