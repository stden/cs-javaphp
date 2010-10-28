package ru.ipo.dces.buildutils;

import java.io.FileNotFoundException;

/**
 * Created by IntelliJ IDEA.
 * User: ilya
 * Date: 27.10.2010
 * Time: 12:59:06
 */
public class GenerateBeans {

    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException {

        String outputFolder = CodeGeneratorSettings.EDITOR_BEANS_FOLDER;
        String outputPackage = CodeGeneratorSettings.EDITOR_BEANS_PACKAGE;

        BinToBeanConverter converter = new BinToBeanConverter("Bean");
        //converter.setBaseClass();

        for (Class<?> c : CodeGeneratorSettings.getBinClasses()) {
            System.out.println("Converting class " + c);
            converter.convert(c);
        }

    }

}
