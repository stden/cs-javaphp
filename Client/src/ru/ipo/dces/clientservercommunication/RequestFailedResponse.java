package ru.ipo.dces.clientservercommunication;

/**
 * Ответ: запрос окончился неудачей
 */
@SuppressWarnings("serial")
public class RequestFailedResponse implements Response {

  enum FailReason {
    Default
  }

  public FailReason failReason = FailReason.Default;

  /**
   * В сообщении можно дать дополнительную информацию об ошибке. Например, при
   * регистрации каких полей не хватает
   */
  public String     message;

  public RequestFailedResponse(String message) {
    this.message = message;
  }

  public RequestFailedResponse() {    
  }
}