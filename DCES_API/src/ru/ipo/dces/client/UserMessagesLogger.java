package ru.ipo.dces.client;

import ru.ipo.dces.client.LogMessageType;

public interface UserMessagesLogger {    

    void log(String message, LogMessageType type, String sender);

}
