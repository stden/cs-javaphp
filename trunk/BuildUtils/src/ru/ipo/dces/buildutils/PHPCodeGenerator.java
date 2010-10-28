package ru.ipo.dces.buildutils;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: ilya
 * Date: 17.10.2010
 * Time: 18:21:47
 */
public class PHPCodeGenerator {

    public static void main(String[] args) throws ClassNotFoundException, FileNotFoundException {

        PHPMocks.generatePHPMocks();
        PHPMocks.generatePHPKeys();

    }


}
