package ru.ipo.dces.buildutils;

import ru.ipo.dces.buildutils.raw.BinInfo;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: ilya
 * Date: 12.11.2010
 * Time: 19:49:31
 */
public class APIGenerator {

    public static void main(String[] args) throws IOException {
        //prepare constant
        final String binInfoSimpleName = BinInfo.class.getSimpleName();
        final String binInfoLineStart = '@' + binInfoSimpleName + '(';

        //go to bins folder
        File binsFolder = new File(CodeGeneratorSettings.BINS_SRC_FOLDER);
        File[] binFiles = binsFolder.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".java") && !name.equals(binInfoSimpleName);
            }
        });

        //copy files except bininfo annotation
        for (File binFile : binFiles) {
            BufferedReader bin = new BufferedReader(new FileReader(binFile));
            PrintStream newBin = new PrintStream(CodeGeneratorSettings.BINS_SRC_OUT_FOLDER + binFile.getName(), "UTF-8");

            //skip the first package line
            bin.readLine();

            //output package and generation info
            newBin.println("package " + CodeGeneratorSettings.BINS_OUT_PACKAGE + ";");
            newBin.println("/*" + CodeGeneratorSettings.HEADER + "*/");

            String line;
            boolean insideBinInfo = false;
            while ((line = bin.readLine()) != null) {
                if (insideBinInfo) {
                    //test line to be ')'
                    if (line.trim().equals(")"))
                        insideBinInfo = false;
                } else {
                    //test line to be
                    // 1) @BinInfo(
                    // 2) @BinInfo(...............)
                    if (line.trim().equals(binInfoLineStart)) {
                        insideBinInfo = true;
                    } else if (!line.trim().startsWith(binInfoLineStart)) {
                        newBin.println(line);
                    }
                }
            }
            
            bin.close();
            newBin.close();
        }
    }

}
