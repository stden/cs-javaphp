package ru.ipo.dces.log;

/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 29.04.2009
 * Time: 14:04:20
 */
public class LoggerFactory {

  private static UserMessagesLogger logger = new ConsoleUserMessagesLogger();

  public static UserMessagesLogger getLogger() {
    return logger;
  }

  public static void setLogger(UserMessagesLogger logger) {
    LoggerFactory.logger = logger;
  }

}
