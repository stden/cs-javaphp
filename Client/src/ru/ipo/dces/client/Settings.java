/**
 * Created by IntelliJ IDEA.
 * User: Илья
 * Date: 24.11.2008
 * Time: 23:17:53
 */
package ru.ipo.dces.client;

import javax.swing.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;

public class Settings {
  private static Settings ourInstance = new Settings();
  private static final String SETTINGS_FILE_NAME = "dces-settings.txt";
  private static String WORKING_DIRECTORY;
  private String PLUGINS_DIRECTORY;
  private Proxy proxy = null;

  private static String PROBLEMS_DIRECTORY;

  private Properties properties;

  public static Settings getInstance() {
    return ourInstance;
  }

  @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
  private Settings() {
    File workDir = new File(".");
    try {
      WORKING_DIRECTORY = workDir.getCanonicalPath();
      PROBLEMS_DIRECTORY = WORKING_DIRECTORY + "/problems";
      PLUGINS_DIRECTORY = WORKING_DIRECTORY + "/plugins";

      new File(PROBLEMS_DIRECTORY).mkdir();
      new File(PLUGINS_DIRECTORY).mkdir();

    } catch (IOException e) {
      dieWithError("Failed to get current directory\nError : " + e.getMessage());
    }

    try {
      properties = new Properties();
      properties.load(new FileInputStream(WORKING_DIRECTORY + '/' + SETTINGS_FILE_NAME));
    } catch (Exception e) {
      dieWithError("Failed to read settings from file 'dces-settings.txt'\nError : " + e.getMessage());
    }
  }

  public String getHost() {
    return properties.getProperty("contest_server") + "dces.php";    
  }

  public String getWorkingDirectory() {
    return WORKING_DIRECTORY;
  }

  public String getProblemsDirectory() {
    return PROBLEMS_DIRECTORY;
  }

  public String getPluginsDirectory() {
    return PLUGINS_DIRECTORY;
  }

  private void dieWithError(String message)
  {
      JOptionPane.showMessageDialog(null, message);
      System.exit(1);
  }

  public Proxy getProxy() {
    //return proxy if created
    if (proxy != null)
      return proxy;

    //create new proxy

    //get proxy type
    String proxyType = (String) properties.get("proxy_type");
    if (proxyType == null)
      return null;

    //try to get proxy type
    Proxy.Type type = null;
    try {
      type = Proxy.Type.valueOf(proxyType);
    } catch (IllegalArgumentException e) {
      dieWithError("Failed to create proxy\nError : " + e.getMessage());
    }

    if (type == Proxy.Type.DIRECT)
      return null;

    //try to get host and port
    String proxyHost = (String) properties.get("proxy_host");
    String proxyPort = (String) properties.get("proxy_port");

    //extract port number
    int port = 0;
    try {
      port = Integer.parseInt(proxyPort);
    } catch (NumberFormatException e) {
      dieWithError("Proxy port " + proxyPort + " is not a number");
    }

    if (proxyHost == null || proxyPort == null)
      dieWithError("Failed to create proxy with host " + proxyHost + " and port " + proxyPort);

    proxy = new Proxy(type, new InetSocketAddress(proxyHost, port));
    return proxy;
  }

}
