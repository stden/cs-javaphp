package ru.ipo.dces.utils;

import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.pluginapi.DCESPluginLoadable;
import ru.ipo.dces.clientservercommunication.InstallClientPluginRequest;
import ru.ipo.dces.clientservercommunication.InstallClientPluginResponse;
import ru.ipo.dces.server.ServerFacade;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarInputStream;
import java.util.jar.JarEntry;

import sun.awt.shell.ShellFolder;

/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 04.05.2009
 * Time: 18:01:07
 */
public class PluginUtils {

  /**
   * Gets plugin's class by plugin's alias. Downloads plugin from server if needed.
   * Plugins folder is a folder whith already downloaded plugins. The method serches a plugin there first, and
   * then downloads it from server, if there is no such plugin.
   * Plugins folder may be null, in this case method always downloads a plugin from server.
   * Class is always loaded from a temporary copy of a pluginjar file, so files in the plugin folder
   * may always be overriden and even deleted
   * @param server server to get plugin from
   * @param plugin_alias alias of the plugin
   * @param pluginsFolder folder to search for plugin, and write plugin there if it was not there
   * @return class of the plugin or null if error occured
   */
  public static Class<? extends Plugin> forceGetPluginClass(ServerFacade server, String plugin_alias, File pluginsFolder) {
    try {
      File pluginFile;
      if (pluginsFolder != null)
        pluginFile = new File(pluginsFolder.getCanonicalPath() + "/" + plugin_alias + ".jar");
      else
        pluginFile = null;

      final File tempPluginFile = ShellFolder.createTempFile(plugin_alias + "_", ".jar");
      tempPluginFile.deleteOnExit();

      //download plugin to temporary file if needed
      if (pluginFile == null || !pluginFile.exists()) {
        //load file from server
        final InstallClientPluginRequest installRequest = new InstallClientPluginRequest();
        installRequest.clientPluginAlias = plugin_alias;
        final InstallClientPluginResponse installResponse = server.doRequest(installRequest);

        //write PluginFile to temporary location

        FileOutputStream fout = new FileOutputStream(tempPluginFile);
        fout.write(installResponse.pluginInstaller);
        fout.close();
      }

      if (pluginFile != null) {
        if (!pluginFile.exists()) {
          if (!FileSystemUtils.copyFile(tempPluginFile, pluginFile)) return null;
        } else {
          if (!FileSystemUtils.copyFile(pluginFile, tempPluginFile)) return null;
        }
      }

      pluginFile = tempPluginFile;

      //the strategy is:
      //1. To load JAR file
      //2. load all packages
      //3. find a class with @loadable annotation
      //4. check for Plugin inheritance
      //5. return it
      URL pluginURL = pluginFile.toURI().toURL();
      JarInputStream jf = new JarInputStream(new FileInputStream(pluginFile));

      URLClassLoader classLoader = new URLClassLoader(new URL[]{pluginURL});

      Class<?> oClass;
      while (true) {
        JarEntry jarEntry = jf.getNextJarEntry();
        if (jarEntry == null) break;

        String className = jarEntry.getName();

        if (className.endsWith(".class")) {
          try {
            oClass = classLoader.loadClass(className.substring(0, className.length() - ".class".length()).replace('/','.'));

            if (oClass.isAnnotationPresent(DCESPluginLoadable.class))
              return oClass.asSubclass(Plugin.class);
          }
          catch (Exception e) {
            //do nothing
          }
        }
      }

      return null;
    } catch (Exception e) {
      return null;
    }
  }

}
