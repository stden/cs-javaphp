package ru.ipo.dces.log;

public interface UserMessagesLogger {    

    void log(String message, LogMessageType type, String sender);

}