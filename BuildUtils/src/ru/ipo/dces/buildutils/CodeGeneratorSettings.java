package ru.ipo.dces.buildutils;

import ru.ipo.dces.buildutils.raw.BinInfo;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by IntelliJ IDEA.
 * User: ilya
 * Date: 27.10.2010
 * Time: 13:14:37
 */
public class CodeGeneratorSettings {

    public static String DCES_API_FOLDER = "out/production/DCES_API/";
    public static String KEY_CLASS_NAME = "ru.ipo.dces.results.Key";

    //must contain / at the end
    public static final String SERVER_MOCKS_FOLDER = "Server/mocks/";
    public static final String SERVER_KEYS_SETTINGS_FILE = "Server/data/Keys.php";

    //bins
    public static final String BINS_FOLDER = "out/production/DCES_API/ru/ipo/dces/clientservercommunication";
    public static final String BINS_PACKAGE = "ru.ipo.dces.buildutils.raw";
    public static final String BINS_SRC_FOLDER = "BuildUtils/src/ru/ipo/dces/buildutils/raw/";
    public static final String BINS_SRC_OUT_FOLDER = "DCES_API/src/ru/ipo/dces/clientservercommunication/";
    public static final String BINS_OUT_PACKAGE = "ru.ipo.dces.clientservercommunication";

    //must contain / at the end
    public static final String EDITOR_BEANS_FOLDER = "Client/src/ru/ipo/dces/serverbeans/";
    public static final String EDITOR_BEANS_PACKAGE = "ru.ipo.dces.serverbeans";
    private static final String UNIQUE_VARIABLE = "__var";

    public static final String REGISTRATOR_CLASS_NAME = "BeansRegistrator";
    public static String HEADER = "This code was automatically generated by code generation facilities of BuildUtils module.\n"
            + "Don't modify it manually";
    public static String INDENT = "    ";

    public static Class<?>[] getBinClasses() throws ClassNotFoundException {
        File[] files = new File(CodeGeneratorSettings.BINS_FOLDER).listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".class");
            }
        });

        Class<?>[] classes = new Class[files.length];
        for (int i = 0; i < files.length; i++) {
            classes[i] = Class.forName(
                    CodeGeneratorSettings.BINS_PACKAGE + '.' +
                            //remove .class from the end of the file
                            files[i].getName().substring(0, files[i].getName().length() - 6)
            );
        }

        return classes;
    }

    public static String getUniqueVariable(int index) {
        if (index == 0)
            return UNIQUE_VARIABLE;
        else
            return UNIQUE_VARIABLE + index + "__";
    }

    public static String getUniqueVariable() {
        return getUniqueVariable(0);
    }
}
