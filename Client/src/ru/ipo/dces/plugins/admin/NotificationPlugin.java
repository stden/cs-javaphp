package ru.ipo.dces.plugins.admin;

import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.pluginapi.PluginEnvironment;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: 04.12.2008
 * Time: 17:45:46
 */
public class NotificationPlugin extends Plugin {


    /**
     * Инициализация plugin'а
     * @param env environment for plugin
     */
    public NotificationPlugin(PluginEnvironment env) {
        super(env);
    }

    protected void fireNotificationMessage(JLabel infoMessageLabel, String s, NotificationType type) {

        infoMessageLabel.setText(s);

        switch (type) {
            case Error:
                infoMessageLabel.setForeground(new Color(255, 100, 100));
                break;
            case Info:
                infoMessageLabel.setForeground(Color.BLACK);
                break;
            case Warning:
                infoMessageLabel.setForeground(new Color(100, 255, 255));
                break;
        }
    }
}
