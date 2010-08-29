//package ru.ipo.dces.tests;
//
//import java.io.IOException;
//import java.util.Random;
//
//import org.junit.Test;
//
//import ru.ipo.dces.server.http.HttpServer;
//import ru.ipo.dces.clientservercommunication.RequestFailedResponse;
//import ru.ipo.dces.tests.samples.Sum;
//
//import static org.junit.Assert.assertEquals;
//
//public class TestHTTP {
//  static final boolean        localTests    = false;
//  private static final String ServerBaseURL = localTests ? "http://localhost:3569/"
//                                                : "http://ipo.spb.ru/dces/";
//  public static final String  ServerURL     = ServerBaseURL + "x.php";
//
//  @Test
//  public void testAllSymbols() throws IOException {
//    // Скрипт, который просто показывает строку
//    final HttpServer sc = new HttpServer(ServerBaseURL + "str.php");
//    // Вывод отдельной строки
//    assertEquals("123", sc.doPost("a=123"));
//    // Специальные символы
//    assertEquals("&!`%", sc.doPost("a=" + sc.StringPrepare("&!`%")));
//    // Символ " - двойная кавычка
//    assertEquals("\"", sc.doPost("a=\""));
//
//    // Символы "по-одному"
//    for (int code = 0; code < 128; code++)
//      if (code != 13 && code != 10 && code != 128) {
//        final String s = "" + (char) code;
//        final String result = sc.doPost("a=" + sc.StringPrepare(s));
//        if (!result.equals(s)) {
//          System.out.println("code = " + code / 16 + "|" + code % 16 + "  s = "
//              + s + " res = " + result);
//          System.out.println("-- " + sc.doPost("code=" + sc.StringPrepare(s)));
//        }
//        assertEquals(s, result);
//      }
//
//    // Русские буквы
//    assertEquals("Привет!", sc.doPost("a=Привет!"));
//
//    // Все символы сразу
//    /*
//     * String s = "", rev_str = ""; for (int code = 0; code < 10; code++) { s +=
//     * (char) code; rev_str = (char) code + rev_str; } assertEquals(rev_str,
//     * sc.doPost("a=" + sc.StringPrepare(s)));
//     *
//     * System.out.println(s);
//     */
//  }
//
//  @Test
//  // Сумма с применением сериализации
//  public void testSum_serialize() throws IOException, IllegalArgumentException,
//      IllegalAccessException, Exception, InstantiationException,
//      NoSuchFieldException {
//    // Скрипт, который суммирует числа с использованием сериализации /
//    // десериализации
//    final Sum s = new Sum();
//    final Random random = new Random();
//    s.a = random.nextInt() % 10000;
//    s.b = random.nextInt() % 10000;
//    final HttpServer sc = new HttpServer(ServerURL);
//    final int i = sc.doRequest(Integer.class, s);
//    assertEquals(s.a + s.b, i);
//  }
//
//  @Test
//  public void testSumHTTP() throws IOException {
//    final HttpServer sc = new HttpServer(ServerBaseURL + "sum.php");
//    assertEquals("sum=13", sc.doPost("a=11&b=2"));
//    final Random random = new Random();
//    final int a = random.nextInt() % 10000;
//    final int b = random.nextInt() % 10000;
//    assertEquals("sum=" + (a + b), sc.doPost("a=" + a + "&b=" + b));
//  }
//
//}
