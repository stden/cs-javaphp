package ru.ipo.dces.client;

import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.utils.PluginUtils;

import java.util.HashMap;
import java.io.File;

public class PluginLoader {

  private static HashMap<String, Class<? extends Plugin>> alias2class = new HashMap<String, Class<? extends Plugin>>();

  /*
  //TODO think how to remove temporary files with plugins, they were created in PluginUtils by ShellFile.createTemporaryFile()
  static {
    //Remove temporary files
    File tempDir = new File(Settings.getInstance().getPluginsTempDirectory());

    File[] files = tempDir.listFiles();
    for (File file : files)
      file.delete();
  }*/

  public static Class<? extends Plugin> getPluginClass(String plugin_alias) {
    Class<? extends Plugin> pluginClass = alias2class.get(plugin_alias);
    if (pluginClass == null) {
      pluginClass = PluginUtils.forceGetPluginClass(
              Controller.getServer(),
              plugin_alias,
              new File(Settings.getInstance().getPluginsDirectory())
      );
      alias2class.put(plugin_alias, pluginClass);
    }
    return pluginClass;
  }

  /**
   * Это Singleton и его не надо лишний раз создавать
   */
  private PluginLoader() {
  }

  public static void clear() {
    alias2class.clear();
  }

}