package ru.ipo.problemsapi;

/**
 * Created by IntelliJ IDEA.
 * User: Ilya
 * Date: 25.03.2010
 * Time: 0:06:37
 */
public interface Problem {  
  public ProblemEntry getEntry(String path);
  public ProblemEntry[] getEntriesByLabel(String label);
  public Problem generate();
}
