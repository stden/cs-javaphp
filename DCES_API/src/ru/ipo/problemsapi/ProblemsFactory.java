package ru.ipo.problemsapi;

import ru.ipo.problemsapi.impl.ProblemZipFileImpl;

import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Ilya
 * Date: 25.03.2010
 * Time: 21:56:37
 */
public class ProblemsFactory {

  public static Problem loadProblem(String fname) throws IOException {
    return loadProblem(new File(fname));
  }

  public static Problem loadProblem(File file) throws IOException {
    return new ProblemZipFileImpl(file);
  }

}
