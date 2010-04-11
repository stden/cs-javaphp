package ru.ipo.problemsapi;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Ilya
 * Date: 25.03.2010
 * Time: 0:20:41
 */
public interface ProblemEntry {

  public String getPath();
  public String getData();
  public InputStream getBinaryStream();
  String setData();
  OutputStream setBinaryStream();

}