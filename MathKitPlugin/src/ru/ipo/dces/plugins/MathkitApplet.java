package ru.ipo.dces.plugins;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AudioClip;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

import ru.ipo.dces.exceptions.GeneralRequestFailureException;
import ru.ipo.dces.pluginapi.PluginEnvironment;
import sun.plugin.javascript.JSContext;
import netscape.javascript.JSObject;


public class MathkitApplet extends JPanel implements Runnable, AppletContext, JSContext {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  Thread t;
  JApplet myApplet = null;
  MyAppletStub myStub = null;
  //JFrame frame;
  String path;
  DCESJSObject obj = new DCESJSObject();
  PluginEnvironment e;
  HashMap<String, String> solution = new HashMap<String, String>();
 

  class DCESJSObject extends JSObject {

    public Object getMember(String name) {
      return new Object();
    }

    public Object getSlot(int index) {
      return new Object();
    }

    public void setMember(String name, Object value) {
    }

    public void setSlot(int index, Object value) {
    }

    public void removeMember(String name) {
    }

    public Object call(String methodName, Object args[]) {
      return new Object();
    }

    public Object eval(String s) {
      int i = s.indexOf("(");
      String m = s.substring(0, i);
      char args[] = new char[s.length() - i];
      String arg1 = "", arg2 = "";

      s.getChars(i, s.length(), args, 0);

      for (char arg : args) {
        if (arg != '(' && arg != ')') {

          if (arg == ',') {
            arg1 = arg2;
            arg2 = "";
          } else {
            arg2 = arg2 + arg;
          }
        }
      }
      arg1=arg1.trim();
      arg2=arg2.trim();
      if(arg1.endsWith("'") && arg1.startsWith("'"))arg1=(arg1.substring(1,arg1.length()-1));
      if(arg2.endsWith("'") && arg2.startsWith("'"))arg2=(arg2.substring(1,arg2.length()-1));
  	  if (m.equals("SCORM.setDataValue")) {
        solution.put(arg1, arg2);

      } else if (m.equals("SCORM.commitData")) {
        //System.out.println("commitData"+solution);
        //solution=new HashMap();
        if (JOptionPane.showConfirmDialog(null, "Подтвердите отсылку решения") == JOptionPane.YES_OPTION) {
          try {
            System.out.println("solution.toString() = " + solution.toString());
            e.submitSolution(solution);
            JOptionPane.showMessageDialog(null, "Решение удачно отослано");
          } catch (GeneralRequestFailureException e) {
            JOptionPane.showMessageDialog(null, "Решение не послано всвязи с проблемами со связью");
          }
        }
        solution = new HashMap<String, String>();
      }
      return new Object();
    }

  }

  class NameFilter implements FileFilter {
    private String mask;

    NameFilter(String mask) {
      this.mask = mask;
    }

    public boolean accept(File file) {
      return (file.getName().indexOf(mask) != -1);
    }
  }

  File result = null;

  public void getFolderName(File file, FileFilter filter)
          throws IOException {
    if (file.isDirectory()) {
      File[] list = file.listFiles();
      for (int i = list.length; --i >= 0;) {
        getFolderName(list[i], filter);
      }
    } else {
      if (filter.accept(file)) {
        result = new File(file.getParent());

      }
    }

  }

  public File getProblemFolder(File file, FileFilter filter)
          throws IOException {
    getFolderName(file, filter);
    return result;

  }

  void setPathApplet() throws IOException {
    try {
      Class<?> c;
      e = PluginInt.pe;
      File APPLET_ROOT = e.getProblemFolder();  
      AppletParameters ap = new AppletParameters(APPLET_ROOT);

      c = Class.forName("rus.sketchpad.gui.SketchpadApplet");
      myApplet = (JApplet) c.newInstance();
      myStub = new MyAppletStub(myApplet, MathkitApplet.this, ap.parameters());
      myApplet.setStub(myStub);

      t = new Thread() {
        public void run() {
          myApplet.init();
          validate();
          myApplet.start();
          validate();


        }
      };
      this.add(myApplet);
      t.start();

    }
    catch (MalformedURLException ex) {
      Logger.getLogger(MathkitApplet.class.getName()).log(Level.SEVERE, null, ex);
    }
    catch (ClassNotFoundException ex) {
      Logger.getLogger(MathkitApplet.class.getName()).log(Level.SEVERE, null, ex);
    }
    catch (InstantiationException ex) {
      Logger.getLogger(MathkitApplet.class.getName()).log(Level.SEVERE, null, ex);
    }
    catch (IllegalAccessException ex) {
      Logger.getLogger(MathkitApplet.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void showStatus(String s) {
  }

  public AudioClip getAudioClip(URL url) {
    return null;
  }

  public Image getImage(URL url) {
    return Toolkit.getDefaultToolkit().createImage(url);
  }

  public Applet getApplet(String name) {
    return null;
  }

  @SuppressWarnings("unchecked")
  public Enumeration getApplets() {
    return null;
  }

  public void showDocument(URL url) {
  }

  public void showDocument(URL url, String target) {
  }

  public void setStream(String key, InputStream stream) throws IOException {

  }

  public InputStream getStream(String key) {
    return null;
  }

  public Iterator<String> getStreamKeys() {
    return null;
  }

  public void run() {
  }

  public JSObject getJSObject() {
    try {
      throw new Exception("get js object");
    } catch (Exception e) {
      e.printStackTrace();
    }
        
    return obj;
  }
}
