package testClient;

import java.io.IOException;
import java.net.*;

import javax.net.ssl.HttpsURLConnection;

import org.junit.Test;

public class TestHTTPS {
  @Test
  void TTT() throws MalformedURLException, IOException {
    HttpsURLConnection conn = (HttpsURLConnection) (new URL(
        "https://www.sun.com/")).openConnection();
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
    int code = conn.getResponseCode();
    System.out.println("Code = " + code);

  }

}
