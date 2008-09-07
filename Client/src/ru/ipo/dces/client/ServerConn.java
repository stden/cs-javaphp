package ru.ipo.dces.client;

import java.io.*;
import java.net.*;
import java.util.HashMap;

public class ServerConn {

  private final String              URL_string;

  static HashMap<Character, String> rep = new HashMap<Character, String>();

  static {
    rep.put('%', "%25");
    rep.put('&', "%26");
    rep.put((char) 0x00, "%00");
    rep.put('+', "%2B");
    rep.put((char) 0x0A, "%0A");
    rep.put((char) 0x0D, "%0D");
  }

  public ServerConn(String URL_string) {
    this.URL_string = URL_string;
  }

  public String doPost(String sendToServer) throws MalformedURLException,
      IOException {
    HttpURLConnection con = (HttpURLConnection) new URL(URL_string)
        .openConnection();
    con.setDoInput(true);
    con.setUseCaches(false);

    con.setRequestMethod("POST");
    con.setDoOutput(true);

    PrintWriter out = new PrintWriter(con.getOutputStream(), true);
    out.println(sendToServer);

    // Read answer
    BufferedReader in = new BufferedReader(new InputStreamReader(con
        .getInputStream()));
    if (con.getResponseCode() != HttpURLConnection.HTTP_OK)
      throw new ConnectException("���������� � " + URL_string + " �� �������!");
    StringBuffer pageContents = new StringBuffer();
    String curLine = in.readLine();
    while (curLine != null) {
      pageContents.append(curLine);
      curLine = in.readLine();
    }
    String result = pageContents.toString();
    return result;
  }

  // ���������� ������ � �������� � GET/POST �������
  public String StringPrepare(String string) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < string.length(); i++)
      if (rep.containsKey(string.charAt(i)))
        sb.append(rep.get(string.charAt(i)));
      else
        sb.append(string.charAt(i));
    return sb.toString();
  }
}