package ru.ipo.dces.client;

/** Разбор строки */
public class StrTokenizer {

  private final String str;
  private int          pos;

  public StrTokenizer(String str) {
    this.str = str;
    pos = 0;
  }

  public char curChar() {
    return str.charAt(pos);
  }

  public String nextToken(char delim) {
    int endIndex = str.indexOf(delim, pos);
    if (endIndex == -1)
      return str.substring(pos);
    int beginIndex = pos;
    pos = endIndex + 1;
    return str.substring(beginIndex, endIndex);
  }

  public String nextToken(char... delims) {
    String result = nextToken(delims[0]);
    for (int i = 1; i < delims.length; i++)
      nextToken(delims[i]);
    return result;
  }

  public String nextToken(int bytesCount) {
    StringBuilder sb = new StringBuilder();
    int bytesProcessed = 0;
    while (bytesProcessed < bytesCount) {
      final char c = str.charAt(pos++);
      sb.append(c);
      //TODO think of an optimization
      bytesProcessed = sb.toString().getBytes(PHP.SERVER_CHARSET).length;
    }

    if (bytesProcessed > bytesCount) throw new IllegalArgumentException("Number of bytes to read means noninteger number of String symbols");

    return sb.toString();
  }

  @Override
  public String toString() {
    return str.substring(0, pos) + "#" + str.substring(pos);
  }
}
