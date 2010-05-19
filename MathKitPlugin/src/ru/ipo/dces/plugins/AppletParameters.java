package ru.ipo.dces.plugins;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

public class AppletParameters {
  private Hashtable<String, String> parameters = new Hashtable<String, String>();
  //String l;

  public AppletParameters(File loc) throws /*MalformedURLException, */IOException {

    String cb = "/" + loc.getName() + "/";
    String mk = null;
    String as = null;
    //String jar = "lib/mathkit-applet.jar";
    File[] list = loc.listFiles();
    for (int i = list.length; --i >= 0;) {
      if (list[i].getName().indexOf(".mkz") != -1) {
        mk = list[i].getCanonicalPath().substring(2).replace("\\", "/");
      } else if (list[i].getName().indexOf("applet-settings.xml") != -1) {
        as = list[i].getCanonicalPath().substring(2).replace("\\", "/");
      }
    }
    parameters.put("documentBase", loc.getCanonicalPath().replace("\\", "/") + "/mathkit-applet.jar!/");
    parameters.put("applet.settings", as);
    parameters.put("message-resources", "/resources/message_resources_ru.properties");
    parameters.put("codebase", cb);
    parameters.put("mayscript", "false");
    parameters.put("construction", mk);
    //parameters.put("archive", jar);
    parameters.put("user.language", "ru");
    parameters.put("code", "rus.sketchpad.gui.SketchpadApplet");
//       parameters.put("code", "sketchpad.gui.SketchpadApplet");
    parameters.put("main-panel.showStatus", "true");
    //  System.out.println("AppletParameters are: "+parameters);

  }

  public Hashtable<String, String> parameters() {
    return parameters;
  }

  /*
  public String archive() {
    return parameters.get("archive");
  }

  public String code() {
    return parameters.get("code");
  }
  */
}
