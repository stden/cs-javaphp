package ru.ipo.dces.log;

public class ConsoleUserMessagesLogger implements UserMessagesLogger {

    public void log(String message, LogMessageType type, String sender) {
        if (sender == null)
            sender = "system";
        System.out.printf("%s: [%s] - %s\n", sender, type.toString().toUpperCase(), message);
    }
}
