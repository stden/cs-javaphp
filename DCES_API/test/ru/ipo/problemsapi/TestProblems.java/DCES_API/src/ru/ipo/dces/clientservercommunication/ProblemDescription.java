package ru.ipo.dces.clientservercommunication;

/**
 * ќписание задачи. Ќе €вл€етс€ запросом или ответом, но используетс€ в них
 */
public class ProblemDescription {

  /** идентификатор задачи*/
  @PHPDefaultValue("-1")
  public int    id;

  /** »дентификатор плагина стороны клиента, с помощью которого участник будет решать задачу */
  @PHPDefaultValue("'Test client plugin'")
  public String clientPluginAlias;

  /** »дентификатор плагина стороны сервера, который будет обрабатывать ответы участника */
  @PHPDefaultValue("'Test server plugin'")
  public String serverPluginAlias;

  /** Ќазвание задачи */
  @PHPDefaultValue("'Test problem'.rand(0,239239)")
  public String name;

  /**
   * ”словие задачи. Ёто конкретное условие задачи дл€ некоторого участника. ѕоле заполн€етс€ сервером в его
   * ответах, в запросах клиента поле игнорируетс€
   */
  @PHPDefaultValue("null")
  public byte[] statement;

  /**
   * ”словие задачи. Ёто данные, которые используютс€ дл€ создани€ услови€.
   */
  @PHPDefaultValue("null")
  public byte[] statementData;

  /**
   * ќтвет к задаче. Ёто данные, которые используютс€ дл€ создани€ ответа
   */
  @PHPDefaultValue("null")
  public byte[] answerData;

}
