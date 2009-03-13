package ru.ipo.dces.client;

import java.io.InputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Илья
 * Date: 23.11.2008
 * Time: 18:02:19
 */
public class StreamTokenizer {

  private final InputStream in;

  public StreamTokenizer(InputStream in) {
    this.in = in;
  }

  public void moveTo(char delim) throws IOException {
    int n;
    while (true) {
      n = in.read();
      if (n == -1) return;
      if (n == delim) return;
    }
  }

  public String readToken(char delim) throws IOException {
    StringBuffer sb = new StringBuffer(64);
    int cnt = 0;
    int n;
    while (true) {
      n = in.read();
      if (n == -1) break;
      if (n == delim) break;
      sb.append((char)n);

      cnt ++;
      if (cnt > 64) throw new IOException("too long token");
    }
    return sb.toString();
  }

  public String readToken(char delim1, char delim2) throws IOException {
    StringBuffer sb = new StringBuffer(16);
    int n;
    while (true) {
      n = in.read();
      if (n == -1) break;
      if (n == delim1 || n == delim2) break;
      sb.append((char)n);
    }
    return sb.toString();
  }

  public byte[] readBytes(int bytesCount) throws IOException {
    byte[] result = new byte[bytesCount];
    for (int i = 0; i < bytesCount; i++)
      result[i] = (byte) in.read();
    return result;
  }

}
