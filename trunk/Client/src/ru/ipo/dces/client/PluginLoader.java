package ru.ipo.dces.client;

import ru.ipo.dces.clientservercommunication.InstallClientPluginRequest;
import ru.ipo.dces.clientservercommunication.InstallClientPluginResponse;
import ru.ipo.dces.pluginapi.DCESPluginLoadable;
import ru.ipo.dces.pluginapi.Plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.HashMap;
import java.util.Random;

public class PluginLoader {

  private static HashMap<String, Class<? extends Plugin>> alias2class = new HashMap<String, Class<? extends Plugin>>();
  private static Random rnd = new Random();

  static {
    //Remove temporary files
    File tempDir = new File(Settings.getInstance().getPluginsTempDirectory());

    File[] files = tempDir.listFiles();
    for (File file : files)
      file.delete();
  }

  public static Class<? extends Plugin> getPluginClass(String plugin_alias) {
    Class<? extends Plugin> pluginClass = alias2class.get(plugin_alias);
    if (pluginClass == null) {
      pluginClass = forceGetPluginClass(plugin_alias);
      alias2class.put(plugin_alias, pluginClass);
    }
    return pluginClass;
  }
  
  private static Class<? extends Plugin> forceGetPluginClass(String plugin_alias) {
    try {
      File pluginFile = new File(Settings.getInstance().getPluginsDirectory() + "/" + plugin_alias + ".jar");
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

      //move PluginFile to temporary location
      final File tempPluginFile = new File(
              Settings.getInstance().getPluginsTempDirectory() + '/' + getTempPluginName(plugin_alias) + ".jar"
              );
      if (!copyFile(pluginFile, tempPluginFile))
        return null;

      tempPluginFile.deleteOnExit();
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

  private static boolean copyFile(File from, File tu) {
    final int BUFFER_SIZE = 4096;
    byte[] buffer = new byte[BUFFER_SIZE];
    try {
      FileInputStream in = new FileInputStream(from);
      FileOutputStream out = new FileOutputStream(tu);

      int read;
      while ((read = in.read(buffer)) != -1) {
        out.write(buffer, 0, read);
      }

      in.close();
      out.close();
    } catch (IOException e) {
      return false;
    }

    return true;
  }

  /**
   * Это Singleton и его не надо лишний раз создавать
   */
  private PluginLoader() {
  }

  public static void clear() {
    alias2class.clear();
  }

  private static String getTempPluginName(String alias) {
    StringBuilder result = new StringBuilder(alias);
    result.append('_');
    for (int i = 0; i < 20; i++)
      result.append((char)('a' + rnd.nextInt(26)));
    return result.toString();
  }

}