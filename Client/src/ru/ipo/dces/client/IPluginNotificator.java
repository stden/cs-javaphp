package ru.ipo.dces.client;

/**
 * Notifies a client of a message from any of the plug-ins (related to any GUI issues)
 * */
public interface IPluginNotificator {
    void fireNotificationMessage(String s);
}
