package ru.ipo.dces.tests;

import java.io.*;
import java.net.*;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

import org.junit.Test;

import ru.ipo.dces.client.ServerConn;


import static org.junit.Assert.assertEquals;

public class TestHTTPS {
  private HttpsURLConnection OpenConnection(String URL_string)
      throws IOException, MalformedURLException, ProtocolException {
    HttpsURLConnection conn = (HttpsURLConnection) new URL(URL_string)
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
  public void testAllSymbols() throws IOException {
    // Адрес скрипта, который "переворачивает" строку
    ServerConn sc = new ServerConn("http://ipo.spb.ru/svn/rev_str.php");
    // Переворачивание отдельной строки
    assertEquals("321", sc.doPost("a=123"));
    // Специальные символы
    assertEquals("%`!&", sc.doPost("a=" + sc.StringPrepare("&!`%")));
    // Символ " - двойная кавычка
    assertEquals("\"\"", sc.doPost("a=\"\""));

    System.out.println("!!! = " + (char) 0x25);

    // Символы "по-одному"
    for (int code = 0; code < 100; code++) {
      String s = "" + (char) code;
      String result = sc.doPost("a=" + sc.StringPrepare(s));
      if (!result.equals(s))
        System.out.println("code = " + code / 16 + "|" + code % 16 + "  s = "
            + s + " res = " + result);
    }

    // Все символы сразу
    String s = "", rev_str = "";
    for (int code = 0; code < 10; code++) {
      s += (char) code;
      rev_str = (char) code + rev_str;
    }
    assertEquals(rev_str, sc.doPost("a=" + sc.StringPrepare(s)));

    System.out.println(s);
  }

  @Test
  public void testConnect() throws MalformedURLException, IOException {
    HttpsURLConnection conn = OpenConnection("https://www.sun.com/");
    assertEquals(true, conn.getDoOutput());
    assertEquals(HttpURLConnection.HTTP_OK, conn.getResponseCode());
    assertEquals("OK", conn.getResponseMessage());
  }

  @Test
  public void testRead() throws IOException {
    HttpsURLConnection connec = null;
    URL url = new URL("https://www.sun.com/");
    connec = (HttpsURLConnection) url.openConnection();
    connec.setDoInput(true);
    connec.setUseCaches(false);

    // String authentication = proxyUser + ":" + proxyPwd;
    // String encodedPassword = "Basic " + new
    // sun.misc.BASE64Encoder().encode(authentication.getBytes());
    // connec.setRequestProperty("Proxy-Authorization", encodedPassword);

    connec.setRequestMethod("POST");
    connec.setDoOutput(true);

    String msg = "---" + "\r\n";
    PrintWriter out = new PrintWriter(connec.getOutputStream(), true);
    out.println(msg);

    int statusCode = connec.getResponseCode();

    System.err.println("Certificats  --->" + connec.getServerCertificates());
    System.err.println("HEADER --->" + connec.getHeaderFields());

    StringBuffer pageContents = new StringBuffer();
    if (statusCode == HttpURLConnection.HTTP_OK) {
      System.err.println("Connected ...!");

      BufferedReader in = new BufferedReader(new InputStreamReader(connec
          .getInputStream()));

      String curLine = in.readLine();
      while (curLine != null) {
        pageContents.append(curLine);
        System.out.println(curLine);
        curLine = in.readLine();
      }
    }

  }

  @Test
  public void testSumHTTP() throws IOException {
    ServerConn sc = new ServerConn("http://ipo.spb.ru/svn/a.php");
    assertEquals("sum=13", sc.doPost("a=11&b=2"));
    Random random = new Random();
    int a = random.nextInt();
    int b = random.nextInt();
    assertEquals("sum=" + (a + b), sc.doPost("a=" + a + "&b=" + b));
  }

};
