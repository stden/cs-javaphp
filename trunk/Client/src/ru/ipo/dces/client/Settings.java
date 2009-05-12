/**
 * Created by IntelliJ IDEA.
 * User: ����
 * Date: 24.11.2008
 * Time: 23:17:53
 */
package ru.ipo.dces.client;

import ru.ipo.dces.log.LoggerFactory;

import javax.swing.*;
import java.util.Scanner;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;

public class Settings {
  private static Settings ourInstance = new Settings();
  private static final String SETTINGS_FILE_NAME = "dces-settings.txt";
  private static String WORKING_DIRECTORY;
  private String PLUGINS_DIRECTORY;

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
      JOptionPane.showMessageDialog(null, "Failed to get current directory\nError : " + e.getMessage());
      System.exit(1);
    }

    try {
      properties = new Properties();
      properties.load(new FileInputStream(WORKING_DIRECTORY + '/' + SETTINGS_FILE_NAME));
    } catch (Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(null, "Failed to read settings from file 'dces-settings.txt'\nError : " + e.getMessage());      
      System.exit(1);
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

}
