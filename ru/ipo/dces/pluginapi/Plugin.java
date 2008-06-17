package ru.ipo.dces.pluginapi;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Илья
 * Date: 10.06.2008
 * Time: 1:12:41
 */
public interface Plugin {

    /**
     * this method is called when plugin is loaded
     * @param client is an interface to the host client
     * @param panel means panel for plugin to be drawn on 
     */
    void initialize(Client client, JPanel panel);

    /**
     * method is called when the plugin gets on the screen
     */
    void show();

    /**
     * method is called when the plugin gets out of the screen
     */
    void hide();

    /**
     * Returns version of the plugin
     * @return version of the plugin
     */
    int getVersion();
}
