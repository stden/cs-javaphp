package ru.ipo.dces.client;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Илья
 * Date: 18.12.2008
 * Time: 20:30:01
 */
public class FileSystemUtils {

  private FileSystemUtils() {
  }

  public static boolean deleteDir(File dir) {
    if (dir.isDirectory()) {
      String[] children = dir.list();
      for (String aChildren : children) {
        boolean success = deleteDir(new File(dir, aChildren));
        if (!success) {
          return false;
        }
      }
    }

    return dir.delete();
  }

  public static void ensureFileHasPath(File file) {
    file.getParentFile().mkdirs();
  }
}
