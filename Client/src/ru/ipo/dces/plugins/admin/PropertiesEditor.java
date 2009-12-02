package ru.ipo.dces.plugins.admin;

import com.l2fprod.common.demo.Main;
import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertySheetPanel;
import com.l2fprod.common.demo.PropertySheetPage3;
import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.beans.*;
import java.util.HashMap;

import ru.ipo.dces.client.Controller;
import ru.ipo.dces.client.Settings;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;
import ru.ipo.dces.exceptions.ServerReturnedError;
import ru.ipo.dces.log.LogMessageType;
import ru.ipo.dces.pluginapi.PluginEnvironment;
import ru.ipo.dces.server.ServerFacade;
import ru.ipo.dces.server.http.HttpServer;

/**
 * Created by IntelliJ IDEA.
 * User: Ilya
 * Date: 30.07.2009
 * Time: 17:50:09
 */
public class PropertiesEditor extends JPanel {

  static class C {
    public int i;
  }

  public static void main(String[] args) throws Exception {
    try {
      //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
    } catch (Exception e) {
      System.out.println("Failed to set system look and feel");
      System.exit(1);
    }

    //
    Controller.server = new HttpServer(Settings.getInstance().getHost());
    Controller.connectToContest(null, "admin", new char[]{'p', 'a', 's', 's'});

    JFrame f = new JFrame();
    f.setLayout(new BorderLayout());

    PropertySheetPanel psp = new PropertySheetPanel();
    BeanInfo beanInfo = Introspector.getBeanInfo(Contest.class, Object.class);
    psp.setBeanInfo(beanInfo);

    //f.add(psp);
    f.add(new ContestPluginV2(new PluginEnvironment() {
      public void setTitle(String title) {}

      public HashMap<String, String> submitSolution(HashMap<String, String> solution) throws GeneralRequestFailureException {
        return null;
      }

      public File getProblemFolder() {
        return null;
      }

      public String getProblemName() {
        return null;
      }

      public void log(String message, LogMessageType type) {}

      public ServerFacade getServer() {
        return null;
      }

      public String getSessionID() {
        return null;
      }

      public int getProblemID() {
        return 0;
      }
    }).getPanel());
    //f.add(new PropertySheetPage3());

    f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    f.pack();
    f.setVisible(true);


  }

  public static class Contest {
    private int i;
    private String s;
    private int[] ints;

    public int getI() {
      return i;
    }

    public void setI(int i) {
      this.i = i;
    }

    public String getS() {
      return s;
    }

    public void setS(String s) {
      this.s = s;
    }

    public int[] getInts() {
      return ints;
    }

    public void setInts(int[] ints) {
      this.ints = ints;
    }

    public int getInts(int index) {
      return ints[i];
    }

    public void setInts(int index, int element) {
      ints[index] = element;
    }
  }

}