package ru.ipo.dces.client;

public interface UserMessagesLogger {

    public enum LogMessageType {
        OK, Warning, Error
    }

    void log(String message, LogMessageType type, Object sender);

}
