package ru.ipo.dces.client;

public class ConsoleUserMessagesLogger implements UserMessagesLogger {

    public void log(String message, LogMessageType type, Object sender) {
        System.out.printf("%s: [%s] - %s", sender, type.toString().toUpperCase(), message);
    }
}