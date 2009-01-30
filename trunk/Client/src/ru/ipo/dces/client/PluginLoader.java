package ru.ipo.dces.client;

import ru.ipo.dces.clientservercommunication.InstallClientPluginRequest;
import ru.ipo.dces.clientservercommunication.InstallClientPluginResponse;
import ru.ipo.dces.pluginapi.DCESPluginLoadable;
import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.pluginapi.PluginEnvironment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

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

    public static Class<? extends Plugin> getPluginClass(String plugin_alias) {
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

            //the strategy is:
            //1. To load JAR file
            //2. load all packages
            //3. find a class with @loadable annotation
            //4. check for Plugin inheritance
            //5. return it
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
                    catch (ClassCastException e) {
                        return null;
                    }
                    catch (Exception e) {
                        //do nothing
                    }
                }
            }

            //try load plugins class
            //URLClassLoader classLoader = new URLClassLoader(new URL[] { pluginURL });
            //final Class<?> oClass = classLoader.loadClass("ru.ipo.dces.plugins.Main");
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Это Singleton и его не надо лишний раз создавать
     */
    private PluginLoader() {
    }
}