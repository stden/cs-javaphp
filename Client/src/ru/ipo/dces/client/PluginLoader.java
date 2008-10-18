package ru.ipo.dces.client;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.*;

import ru.ipo.dces.pluginapi.*;

public class PluginLoader {

  private static final String PLUGIN_DIR = "..";

  public static Plugin load(String plugin_id, PluginEnvironment pe) {
    try {
      URL pluginURL = new File(PLUGIN_DIR + "/" + plugin_id + ".jar").toURI()
          .toURL();
      URLClassLoader classLoader = new URLClassLoader(new URL[] { pluginURL });
      Class<Plugin> mainClass = (Class<Plugin>) classLoader
          .loadClass("ru.ipo.dces.plugin.Main");
      Constructor<Plugin> constructor = mainClass
          .getConstructor(PluginEnvironment.class);
      return constructor.newInstance(pe);
    } catch (Exception e) {
      return null;
    }
  }

  /** Это Singleton и его не надо лишний раз создавать */
  private PluginLoader() {
  }
}
