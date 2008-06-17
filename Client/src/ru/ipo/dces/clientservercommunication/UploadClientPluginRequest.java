package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: Илья
 * Date: 24.05.2008
 * Time: 14:48:26
 */
public class UploadClientPluginRequest implements InfoFrame {

    public String sessionID;
    public String pluginID;
    public byte[] pluginInstaller;

}
