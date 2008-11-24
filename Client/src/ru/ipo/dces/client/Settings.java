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

public class Settings {
  private static Settings ourInstance = new Settings();
  private static final String FILE_NAME = "dces-settings.txt";

  private String host = "";

  public static Settings getInstance() {
    return ourInstance;
  }

  @SuppressWarnings({"ConstantConditions"})
  private Settings() {
    Scanner s = null;
    try {
      s = new Scanner(new FileInputStream(FILE_NAME));

      int lineNo = 0;
      while (s.hasNextLine()) {
        String line = s.nextLine();
        lineNo ++;
        int pos;
        if ((pos=line.indexOf(';')) != -1)
          line = line.substring(0, pos);

        pos = line.indexOf('=');
        if (pos == -1) throw new Exception("No '=' found in line " + lineNo);
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
}
