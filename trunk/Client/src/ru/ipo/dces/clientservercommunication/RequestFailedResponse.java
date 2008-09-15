package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA. User: Илья Date: 21.05.2008 Time: 3:43:41
 */
@SuppressWarnings("serial")
public class RequestFailedResponse extends Throwable implements InfoFrame {
  public int    failReason; // 0 - контест еще не начался, если пытаешься
  // подключится
  // 1 - контест уже кончился, при попытке подключиться
  // 2 - логин или пароль не соответствуют, при подключении. И т.п.
  // и т.д.
  public String message;   // в сообщении можно дать дополнительную информацию
  // об ошибке. Например,
  // при регистрации каких полей не хватает
}
