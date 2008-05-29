package testClient;

import java.lang.reflect.Array;
import java.util.HashMap;

import junit.framework.Assert;

import org.junit.Test;

import Client.*;

import static org.junit.Assert.assertEquals;

public class TestSerialize {

  public class BigClass {
    public MyClass a;
    public int[]   b;
  }

  public class MyClass {
    public int login;
    public int g;
  }

  private void assertArrayEquals(Object ar, Object arr) {
    Assert.assertTrue(ar.getClass().isArray());
    Assert.assertTrue(arr.getClass().isArray());
    assertEquals(Array.getLength(ar), Array.getLength(arr));
    for (int k = 0; k < Array.getLength(ar); k++)
      assertEquals(Array.get(ar, k), Array.get(arr, k));
  }

  @Test
  public void testNull() throws IllegalAccessException,
      IllegalArgumentException, InstantiationException {
    assertEquals("N;", PHP.serialize(null));
    assertEquals(null, PHP.unserialize(null, "N;"));
  }

  @Test
  public void testPrimitiveTypes() throws IllegalAccessException,
      IllegalArgumentException, InstantiationException {
    // Одиночные переменные - примитивные типы
    for (int a = 1; a < 3; a++)
      assertEquals("i:" + a + ";", PHP.serialize(a));
    for (int i = 1; i < 101; i++) {
      int ii = PHP.unserialize(Integer.class, "i:" + i + ";");
      assertEquals(i, ii);
    }
    long l = 922372036854775807L;
    assertEquals("i:" + l + ";", PHP.serialize(l));
    assertEquals(l, PHP.unserialize(Long.class, PHP.serialize(l)));
    byte b = 21;
    assertEquals("i:" + b + ";", PHP.serialize(b));
    assertEquals(b, PHP.unserialize(Byte.class, PHP.serialize(b)));
    short s = 32767;
    assertEquals("i:" + s + ";", PHP.serialize(s));
    assertEquals(s, PHP.unserialize(Short.class, PHP.serialize(s)));
    char c = 'a';
    assertEquals("s:1:\"a\";", PHP.serialize(c));
    char cc = PHP.unserialize(Character.class, PHP.serialize(c));
    assertEquals(c, cc);
    String tt = "Test string";
    assertEquals("s:11:\"Test string\";", PHP.serialize(tt));
    // assertEquals(s, PHP.unserialize(String.class, PHP.serialize(s)));
  }

  @Test
  public void testSerialize() throws IllegalArgumentException,
      IllegalAccessException, InstantiationException {

    // Вещественные типы переменных
    float f = 1.3232f;
    assertEquals("d:1.3232;", PHP.serialize(f));
    assertEquals(f, PHP.unserialize(Float.class, PHP.serialize(f)));
    double d = 2.34;
    assertEquals("d:2.34;", PHP.serialize(d));
    assertEquals(d, PHP.unserialize(Double.class, PHP.serialize(d)));

    // Булевский тип переменных
    assertEquals("b:1;", PHP.serialize(true));
    assertEquals(true, PHP.unserialize(Boolean.class, "b:1;"));
    assertEquals("b:0;", PHP.serialize(false));
    assertEquals(false, PHP.unserialize(Boolean.class, "b:0;"));

    // Одномерные массивы
    int[] ar = { 2, 3, 9 };
    assertEquals("a:3:{i:0;i:2;i:1;i:3;i:2;i:9;}", PHP.serialize(ar));
    int[] arr = PHP.unserialize(int[].class, PHP.serialize(ar));
    assertArrayEquals(ar, arr);

    String[] sa = { "s1", "s2" };
    assertEquals("a:2:{i:0;s:2:\"s1\";i:1;s:2:\"s2\";}", PHP.serialize(sa));
    // String[] saa = (String[]) PHP.unserialize(PHP.serialize(sa));
    // assertArrayEquals(sa, saa);

    // Многомерные массивы
    int[][] aa = { { 1, 2 }, { 3, 4 } };
    assertEquals("a:2:{i:0;a:2:{i:0;i:1;i:1;i:2;}i:1;a:2:{i:0;i:3;i:1;i:4;}}",
        PHP.serialize(aa));

    // Хеш-таблицы
    HashMap<String, String> hm = new HashMap<String, String>();
    hm.put("a b", "ss 1");
    hm.put("test", "aa 2");
    assertEquals("a:2:{s:3:\"a b\";s:4:\"ss 1\";s:4:\"test\";s:4:\"aa 2\";}",
        PHP.serialize(hm));

    // Классы
    MyClass m = new MyClass();
    m.login = 1;
    m.g = 2;
    assertEquals("O:7:\"MyClass\":2:{s:5:\"login\";i:1;s:1:\"g\";i:2;}", PHP
        .serialize(m));

    // Вложенные классы
    BigClass bc = new BigClass();
    bc.a = new MyClass();
    bc.a.login = 6;
    bc.a.g = 7;
    bc.b = new int[2];
    bc.b[0] = 453;
    bc.b[1] = 64;
    assertEquals(
        "O:8:\"BigClass\":2:{s:1:\"a\";O:7:\"MyClass\":2:{s:5:\"login\";i:6;s:1:\"g\";i:7;}s:1:\"b\";a:2:{i:0;i:453;i:1;i:64;}}",
        PHP.serialize(bc));
  }

  @Test
  public void testTokenizer() throws IllegalArgumentException,
      IllegalAccessException, InstantiationException {
    StrTokenizer st = new StrTokenizer("s:11:\"Test string\";");
    assertEquals('s', st.curChar());
    assertEquals("s", st.nextToken(':'));
    assertEquals('1', st.curChar());
    assertEquals("11", st.nextToken(':', '"'));
    assertEquals("Test string", st.nextToken(11));
    assertEquals('"', st.curChar());
    assertEquals("", st.nextToken('"', ';'));
  }
}
