package ru.ipo.dces.clientservercommunication;

/**
 * Ответ: запрос окончился неудачей
 */
@SuppressWarnings("serial")
public class RequestFailedResponse extends Throwable implements Response {
  /**
   * 0 - контест еще не начался, если пытаешься подключится. 1 - контест уже
   * кончился, при попытке подключиться. 2 - логин или пароль не соответствуют,
   * при подключении. И т.п. и т.д.
   */
  public int    failReason;
  /**
   * В сообщении можно дать дополнительную информацию об ошибке. Например, при
   * регистрации каких полей не хватает
   */
  public String message;

  public RequestFailedResponse(String message) {
    this.message = message;
  }
}
