package ru.ipo.problemsapi.impl;

import ru.ipo.problemsapi.Problem;
import ru.ipo.problemsapi.ProblemEntry;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by IntelliJ IDEA.
 * User: Ilya
 * Date: 25.03.2010
 * Time: 23:33:16
 */
public class ProblemZipFileImpl implements Problem {

  private File zipFile;

  public ProblemZipFileImpl(File file) throws IOException {
    zipFile = file;
    ZipFile zip = new ZipFile(zipFile);
    readManifest(getManifest(zip));
    zip.close();
  }

  private void readManifest(ZipEntry manifest) {
  }

  private ZipEntry getManifest(ZipFile zip) throws IOException {
    final ZipEntry manifest = zip.getEntry("META-INF/manifest.xml");
    if (manifest == null)
      throw new IOException("Failed to load manifest");
    return manifest;
  }

  public ProblemEntry getEntry(String path) {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public ProblemEntry[] getEntriesByLabel(String label) {
    return new ProblemEntry[0];  //To change body of implemented methods use File | Settings | File Templates.
  }

  public Problem generate() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

}
