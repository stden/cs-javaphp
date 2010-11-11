package ru.ipo.dces.buildutils;

import ru.ipo.dces.clientservercommunication.BinInfo;
import ru.ipo.dces.results.Key;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class PHPMocks {

    public static void generatePHPMocks() throws FileNotFoundException, ClassNotFoundException {

        PrintWriter allMocks = new PrintWriter(CodeGeneratorSettings.SERVER_MOCKS_FOLDER + "all_mocks.php");

        allMocks.println("<?php");
        allMocks.println("$preIP = dirname(__FILE__);");

        //constructing php files
        for (Class c : CodeGeneratorSettings.getBinClasses()) {
            ArrayList<String> initStatements = new ArrayList<String>();

            File res = new File(CodeGeneratorSettings.SERVER_MOCKS_FOLDER + c.getSimpleName() + ".php");

            PrintWriter pw = new PrintWriter(res);
            pw.println("<?php");

            //print all require_once in all_mocks.php
            allMocks.println("require_once(\"$preIP/" + c.getSimpleName() + ".php\");");

            //getting all needed require_once statements
            boolean preIPWasPrinted = false;
            for (Field fld : c.getFields()) {
                BinInfo a = fld.getAnnotation(BinInfo.class);

                if (a == null) {
                    continue;
                }

                if (a.phpDefaultValue().equals("")) {
                    if (!preIPWasPrinted) {
                        preIPWasPrinted = true;
                        pw.println("$preIP = dirname(__FILE__);");
                    }
                    pw.println("require_once(\"$preIP/" + fld.getType().getSimpleName() + ".php\");");
                }
            }

            pw.println("class " + c.getSimpleName() + " {");

            //printing fields
            for (Field fld : c.getFields()) {
                BinInfo a = fld.getAnnotation(BinInfo.class);

                String val;

                if (a == null)
                    val = getDefaultValueByType(fld.getType());
                else
                    val = a.phpDefaultValue();

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
    
    public static void generatePHPKeys() throws FileNotFoundException {

        File outFile = new File(CodeGeneratorSettings.SERVER_KEYS_SETTINGS_FILE);

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