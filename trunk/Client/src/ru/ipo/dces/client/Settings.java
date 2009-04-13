/**
 * Created by IntelliJ IDEA.
 * User: Илья
 * Date: 24.11.2008
 * Time: 23:17:53
 */
package ru.ipo.dces.client;

import javax.swing.*;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;

public class Settings {
  private static Settings ourInstance = new Settings();
  private static final String SETTINGS_FILE_NAME = "dces-settings.txt";
  private static String WORKING_DIRECTORY;
  private String PLUGINS_DIRECTORY;
  private String PLUGINS_TEMP_DIRECTORY;

  private static String PROBLEMS_DIRECTORY;
  private String host = "";

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
      PLUGINS_TEMP_DIRECTORY = WORKING_DIRECTORY + "/plugins-temp";

      new File(PROBLEMS_DIRECTORY).mkdir();
      new File(PLUGINS_DIRECTORY).mkdir();
      new File(PLUGINS_TEMP_DIRECTORY).mkdir();

    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, "Failed to get current directory\nError : " + e.getMessage());
      System.exit(1);
    }

    Scanner s = null;
    try {
      s = new Scanner(new FileInputStream(WORKING_DIRECTORY + '/' + SETTINGS_FILE_NAME));

      int lineNo = 0;
      while (s.hasNextLine()) {
        String line = s.nextLine();
        lineNo ++;
        int pos;
        if ((pos=line.indexOf(';')) != -1)
          line = line.substring(0, pos);

        pos = line.indexOf('=');
        if (pos == -1)
          if (line.trim().equals(""))
            continue;
          else
            throw new Exception("No '=' found in line " + lineNo);
        String name = line.substring(0, pos-1).trim();
        String value = line.substring(pos+1).trim();

        if (name.compareToIgnoreCase("contest server") == 0) {
          if (value.equals("")) throw new Exception("Empty value in line " + lineNo);
          if (value.charAt(value.length() - 1) != '/') value += '/';
          value += "dces.php";
          host = value;
        } //else if...
        else throw new Exception("Unknown parameter name '" + name + "' in line " + lineNo);
      }
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, "Failed to read settings from file 'dces-settings.txt'\nError : " + e.getMessage());      
      System.exit(1);
    }
    finally {
      s.close();
    }
  }

  public String getHost() {
    return host;
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

  public String getPluginsTempDirectory() {
    return PLUGINS_TEMP_DIRECTORY;
  }
}
