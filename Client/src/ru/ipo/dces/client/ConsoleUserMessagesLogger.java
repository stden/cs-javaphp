package ru.ipo.dces.client;

public class ConsoleUserMessagesLogger implements UserMessagesLogger {

    public void log(String message, LogMessageType type, String sender) {
        System.out.printf("%s: [%s] - %s\n", sender, type.toString().toUpperCase(), message);
    }
}
