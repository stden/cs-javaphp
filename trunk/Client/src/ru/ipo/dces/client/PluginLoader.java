package ru.ipo.dces.client;

import java.io.File;
import java.lang.reflect.*;
import java.net.*;

import ru.ipo.dces.pluginapi.*;

public class PluginLoader {

  private static final String       PLUGIN_DIR = "..";
  private final static PluginLoader instance   = new PluginLoader();

  public static PluginLoader getInstance() {
    return instance;
  }

  public Plugin loadPlugin(String plugin_id) throws MalformedURLException,
      ClassNotFoundException, InstantiationException, IllegalAccessException,
      IllegalArgumentException, SecurityException, InvocationTargetException,
      NoSuchMethodException {
    URL pluginURL = new File(PLUGIN_DIR + "/" + plugin_id + ".jar").toURI()
        .toURL();
    URLClassLoader classLoader = new URLClassLoader(new URL[] { pluginURL });
    Class<Plugin> mainClass = (Class<Plugin>) classLoader
        .loadClass("ru.ipo.dces.plugin.Main");
    Constructor<Plugin> constructor = mainClass.getConstructor(Client.class);
    return constructor.newInstance(ClientImpl.getInstance());
  }
}
