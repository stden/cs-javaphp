package ru.ipo.dces.buildutils;

import ru.ipo.dces.clientservercommunication.PHPDefaultValue;
import ru.ipo.dces.results.Key;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class PHPMocks {

    public static void generatePHPMocks(String serverMsgsPath, String phpMocksDir, String reqOncePath) throws FileNotFoundException, ClassNotFoundException {

        //gettting dirs
        File f = new File(serverMsgsPath);

        //getting all message classes
        File[] files = f.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".class");
            }
        });

        PrintWriter allMocks = new PrintWriter(phpMocksDir + "all_mocks.php");

        allMocks.println("<?php");

        //constructing php files
        for (File file : files) {
            ArrayList<String> initStatements = new ArrayList<String>();

            Class c = Class.forName("ru.ipo.dces.clientservercommunication." + file.getName().substring(0, file.getName().length() - 6));

            File res = new File(phpMocksDir + c.getSimpleName() + ".php");

            PrintWriter pw = new PrintWriter(res);
            pw.println("<?php");

            //print all require_once in all_mocks.php
            allMocks.println("require_once('" + reqOncePath + c.getSimpleName() + ".php');");

            //getting all needed require_once statements
            for (Field fld : c.getFields()) {
                PHPDefaultValue a = fld.getAnnotation(PHPDefaultValue.class);

                if (a == null) {
                    continue;
                }

                if (a.value().equals("")) {
                    pw.println("require_once('" + reqOncePath + fld.getType().getSimpleName() + ".php');");
                }
            }

            pw.println("class " + c.getSimpleName() + " {");

            //printing fields
            for (Field fld : c.getFields()) {
                PHPDefaultValue a = fld.getAnnotation(PHPDefaultValue.class);

                String val;

                if (a == null)
                    val = getDefaultValueByType(fld.getType());
                else
                    val = a.value();

                pw.println("    public $" + fld.getName() + ";");

                if (val.equals(""))
                    initStatements.add("$this->" + fld.getName() + " = new " + fld.getType().getSimpleName() + "();");
                else
                    initStatements.add("$this->" + fld.getName() + " = " + val + ";");
            }

            if (initStatements.size() > 0) {
                pw.println();
                pw.println("    function __construct(){");
                for (String is : initStatements)
                    pw.println("        " + is);
                pw.println("    }");
            }

            pw.println("};");
            pw.println("?>");

            pw.close();
        }

        allMocks.println("?>");

        allMocks.close();
    }

    private static String getDefaultValueByType(Class<?> type) {
        if (type == String.class)
            return "''";
        /*if (type.isArray())
           return "array()";
         if (type == boolean.class)
           return "false";
         if (type == int.class)
           return "0";*/
        return "null";
    }
    
    public static void generatePHPKeys(String keysFile) throws FileNotFoundException {

        File outFile = new File(keysFile);

        PrintStream out = new PrintStream(outFile);

        out.println("<?php");
        out.println("// this file was automatically generated from data in ru.ipo.dces.results.Key");

        out.println("define('TRANSFERABLE_KEYS', array(");

        Key[] allKeys = Key.getAllKeys();
        for (Key key : allKeys)
            if (key.isTransferable())
                out.printf("    '%s',\n", key.getName());            

        out.println("));");
        out.print("?>");
        out.close();
    }
}