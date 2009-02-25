package ru.ipo.dces.tests;

import java.io.*;
import java.net.*;

import javax.net.ssl.HttpsURLConnection;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestHTTPS {

  private HttpsURLConnection OpenConnection(String URL_string)
      throws IOException, MalformedURLException, ProtocolException {
    final HttpsURLConnection conn = (HttpsURLConnection) new URL(URL_string)
        .openConnection();
    conn.setRequestMethod("GET");
    conn
        .setRequestProperty(
            "User-agent",
            "Mozilla/5.0 (Windows; U; Windows NT 5.1; de; rv:1.8.0.6) Gecko/20060728 Firefox/1.5.0.6");
    conn
        .setRequestProperty(
            "Accept",
            "text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
    conn.setRequestProperty("Accept-Language",
        "de-de,de;q=0.8,en-us;q=0.5,en;q=0.3");
    conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
    conn.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
    conn.setRequestProperty("Keep-Alive", "300");
    // conn.setRequestProperty( "Proxy-Connection","keep-alive");
    conn.setRequestProperty("Cache-Control", "max-age=0");
    // conn.setRequestProperty("Cookie",cookie);
    conn.setConnectTimeout(5000);
    conn.setDoOutput(true);
    return conn;
  }

  @Test
  public void testConnect() throws MalformedURLException, IOException {
    final HttpsURLConnection conn = OpenConnection("https://www.sun.com/");
    assertEquals(true, conn.getDoOutput());
    assertEquals(HttpURLConnection.HTTP_OK, conn.getResponseCode());
    assertEquals("OK", conn.getResponseMessage());
  }

  @Test
  public void testRead() throws IOException {
    HttpsURLConnection connec = null;
    final URL url = new URL("https://www.sun.com/");
    connec = (HttpsURLConnection) url.openConnection();
    connec.setDoInput(true);
    connec.setUseCaches(false);

    // String authentication = proxyUser + ":" + proxyPwd;
    // String encodedPassword = "Basic " + new
    // sun.misc.BASE64Encoder().encode(authentication.getBytes());
    // connec.setRequestProperty("Proxy-Authorization", encodedPassword);

    connec.setRequestMethod("POST");
    connec.setDoOutput(true);

    final String msg = "---" + "\r\n";
    final PrintWriter out = new PrintWriter(connec.getOutputStream(), true);
    out.println(msg);

    final int statusCode = connec.getResponseCode();

    System.err.println("Certificats  --->" + connec.getServerCertificates());
    System.err.println("HEADER --->" + connec.getHeaderFields());

    final StringBuffer pageContents = new StringBuffer();
    if (statusCode == HttpURLConnection.HTTP_OK) {
      System.err.println("Connected ...!");

      final BufferedReader in = new BufferedReader(new InputStreamReader(connec
          .getInputStream()));

      String curLine = in.readLine();
      while (curLine != null) {
        pageContents.append(curLine);
        System.out.println(curLine);
        curLine = in.readLine();
      }
    }

  }

};
