package ru.ipo.dces.utils;

import ru.ipo.dces.clientservercommunication.DownloadPluginResponse;
import ru.ipo.dces.clientservercommunication.PluginSide;
import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.pluginapi.DCESPluginLoadable;
import ru.ipo.dces.clientservercommunication.DownloadPluginRequest;
import ru.ipo.dces.server.ServerFacade;
import ru.ipo.dces.exceptions.ServerReturnedError;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarInputStream;
import java.util.jar.JarEntry;
import java.util.ArrayList;

import sun.awt.shell.ShellFolder;

/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 04.05.2009
 * Time: 18:01:07
 */
public class PluginUtils {

  static {
    JarJarURLConnection.register();
  }

  /**
   * Gets plugin's class by plugin's alias. Downloads plugin from server if needed.
   * Plugins folder is a folder whith already downloaded plugins. The method serches a plugin there first, and
   * then downloads it from server, if there is no such plugin.
   * Plugins folder may be null, in this case method always downloads a plugin from server.
   * Class is always loaded from a temporary copy of a pluginjar file, so files in the plugin folder
   * may always be overriden and even deleted
   *
   * @param server        server to get plugin from
   * @param plugin_alias  alias of the plugin
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
      if (pluginFile == null || !pluginFile.exists())
        loadPluginFromServer(server, plugin_alias, tempPluginFile);

      if (pluginFile != null) {
        if (!pluginFile.exists()) {
          if (!FileSystemUtils.copyFile(tempPluginFile, pluginFile)) return null;
        } else {
          if (!FileSystemUtils.copyFile(pluginFile, tempPluginFile)) return null;
        }
      }

      pluginFile = tempPluginFile;

      JarInputStream jf = new JarInputStream(new FileInputStream(pluginFile));
      ClassLoader classLoader = getPluginClassLoader(pluginFile, jf);
      jf.close();

      jf = new JarInputStream(new FileInputStream(pluginFile));      
      Class<? extends Plugin> pluginClass = findMainPluginClass(jf, classLoader);
      jf.close();
      return pluginClass;
    } catch (Exception e) {
      return null;
    }
  }

  private static ClassLoader getPluginClassLoader(File pluginFile, JarInputStream jf) throws IOException {
    ArrayList<URL> urls = new ArrayList<URL>();
    URL pluginURL = pluginFile.toURI().toURL();
    urls.add(pluginURL);

    //find all dependencies
    JarEntry jarEntry;
    while ((jarEntry = jf.getNextJarEntry()) != null) {
      
      String jarName = jarEntry.getName();

      if (jarName.startsWith("dependencies/") && jarName.endsWith(".jar")) {
        urls.add(new URL("jar:jarjar:" + pluginURL + "%/" + jarName + "!/"));
      }
    }

    return new URLClassLoader(urls.toArray(new URL[urls.size()]));
  }

  private static Class<? extends Plugin> findMainPluginClass(JarInputStream jf, ClassLoader classLoader) throws IOException {
    //the strategy is:
    //1. To load JAR file
    //2. load all packages
    //3. find a class with @loadable annotation
    //4. check for Plugin inheritance
    //5. return it
    Class<?> clazz;
    JarEntry jarEntry;
    while ((jarEntry = jf.getNextJarEntry()) != null) {
      String className = jarEntry.getName();

      if (className.endsWith(".class")) {
        try {
          clazz = classLoader.loadClass(className.substring(0, className.length() - ".class".length()).replace('/', '.'));

          if (clazz.isAnnotationPresent(DCESPluginLoadable.class))
            return clazz.asSubclass(Plugin.class);
        }
        catch (Exception ignored) {}
      }
    }

    return null;
  }

  private static void loadPluginFromServer(ServerFacade server, String plugin_alias, File tempPluginFile) throws ServerReturnedError, GeneralRequestFailureException, IOException {
    //load file from server
    final DownloadPluginRequest installRequest = new DownloadPluginRequest();
    installRequest.side = PluginSide.Client;
    installRequest.pluginAlias = plugin_alias;
    final DownloadPluginResponse installResponse = server.doRequest(installRequest);

    //write PluginFile to temporary location

    FileOutputStream fout = new FileOutputStream(tempPluginFile);
    fout.write(installResponse.pluginBytes);
    fout.close();
  }

}
