package ru.ipo.dces.buildutils;

import ru.ipo.structurededitor.model.DSLBeansRegistry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.Modifier;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: ilya
 * Date: 27.10.2010
 * Time: 12:59:06
 */
public class GenerateBeans {

    //TODO don't generate code for Annotations

    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException {

        BinToBeanConverter converter = new BinToBeanConverter("Bean");
        //converter.setBaseClass();

        LinkedList<String> newClasses = new LinkedList<String>();
        for (Class<?> c : CodeGeneratorSettings.getBinClasses()) {
            if (c.isEnum() || Modifier.isAbstract(c.getModifiers()) || c.isAnnotation())
                continue;
            System.out.println("Converting class " + c);
            String newClass = converter.convert(c);
            newClasses.add(newClass);
        }

        //create beans registrator class
        createRegistratorClass(newClasses);
    }

    private static void createRegistratorClass(LinkedList<String> newClasses) throws FileNotFoundException {
        String registratorPath = CodeGeneratorSettings.EDITOR_BEANS_FOLDER + CodeGeneratorSettings.REGISTRATOR_CLASS_NAME + ".java";
        PrintStream out = new PrintStream(registratorPath);

        out.println("package " + CodeGeneratorSettings.EDITOR_BEANS_PACKAGE + ";");
        out.println("/*" + CodeGeneratorSettings.HEADER + "*/");
        out.println("public class " + CodeGeneratorSettings.REGISTRATOR_CLASS_NAME + " {");
        out.println();
        out.println(CodeGeneratorSettings.INDENT + "public static void register() {");

        //register all classes one by one

        out.printf(
                "%s%s%s reg = \n%s%s%s%s.getInstance();\n\n",
                CodeGeneratorSettings.INDENT,
                CodeGeneratorSettings.INDENT,
                DSLBeansRegistry.class.getCanonicalName(),
                CodeGeneratorSettings.INDENT,
                CodeGeneratorSettings.INDENT,
                CodeGeneratorSettings.INDENT,
                DSLBeansRegistry.class.getCanonicalName()
        );

        for (String newClass : newClasses)
            out.printf(
                "%s%sreg.registerBean(%s.class);\n",
                CodeGeneratorSettings.INDENT,
                CodeGeneratorSettings.INDENT,               
                newClass                     
            );

        out.println(CodeGeneratorSettings.INDENT + "}");
        out.println("}");
    }

}
