package Client;

import java.io.*;
import java.net.*;

public class ServerConn {

  private final String URL_string;

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
      throw new ConnectException("Соединение с " + URL_string + " не удалось!");
    StringBuffer pageContents = new StringBuffer();
    String curLine = in.readLine();
    while (curLine != null) {
      pageContents.append(curLine);
      curLine = in.readLine();
    }
    String result = pageContents.toString();
    return result;
  }

}
