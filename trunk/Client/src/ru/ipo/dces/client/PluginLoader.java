package ru.ipo.dces.client;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Constructor;
import java.net.*;

import ru.ipo.dces.pluginapi.*;
import ru.ipo.dces.clientservercommunication.InstallClientPluginRequest;
import ru.ipo.dces.clientservercommunication.InstallClientPluginResponse;

public class PluginLoader {

  public static Plugin load(String plugin_alias, PluginEnvironment pe) {
    try {
      Class<? extends Plugin> mainClass = getPluginClass(plugin_alias);
      //NullPointerException is possible in the next line
      Constructor<? extends Plugin> constructor = mainClass.getConstructor(PluginEnvironment.class);
      return constructor.newInstance(pe);
    } catch (Exception e) {
      //something wrong with plugins
      return null;
    }
  }

  public static Class<? extends Plugin> getPluginClass(String plugin_alias){
    try {
      final File pluginFile = new File(Settings.getInstance().getPluginsDirectory() + "/" + plugin_alias + ".jar");
      //test if file exists
      if (!pluginFile.exists()) {
      //load file from server
      final InstallClientPluginRequest installRequest = new InstallClientPluginRequest();
      installRequest.clientPluginAlias = plugin_alias;
      final InstallClientPluginResponse installResponse = Controller.getServer().doRequest(installRequest);
      FileOutputStream fout = new FileOutputStream(pluginFile);
      fout.write(installResponse.pluginInstaller);
      fout.close();
    }
      URL pluginURL = pluginFile.toURI().toURL();

      //try load plugins class
      URLClassLoader classLoader = new URLClassLoader(new URL[] { pluginURL });
      final Class<?> oClass = classLoader.loadClass("ru.ipo.dces.plugins.Main");
      return oClass.asSubclass(Plugin.class);
    } catch (Exception e) {
      return null;
    }
  }

  /** Это Singleton и его не надо лишний раз создавать */
  private PluginLoader() {
  }
}