package ru.ipo.dces.tests;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map.Entry;

import junit.framework.Assert;

import org.junit.Test;

import ru.ipo.dces.client.*;


import static org.junit.Assert.assertEquals;

public class TestSerialize {

  public static class HSS extends HashMap<String, String> {
  }

  private void assertArrayEquals(Object ar, Object arr) {
    Assert.assertTrue(ar.getClass().isArray());
    Assert.assertTrue(arr.getClass().isArray());
    assertEquals(Array.getLength(ar), Array.getLength(arr));
    for (int k = 0; k < Array.getLength(ar); k++) {
      Object obj1 = Array.get(ar, k);
      Object obj2 = Array.get(arr, k);
      if (obj1.getClass().isArray() && obj2.getClass().isArray())
        assertArrayEquals(obj1, obj2);
      else
        assertEquals(obj1, obj2);
    }
  }

  private void assertHashMapEquals(HashMap<?, ?> expected, HashMap<?, ?> actual) {
    assertEquals("HashMap size: ", expected.size(), actual.size());
    for (Entry<?, ?> entry : expected.entrySet())
      assertEquals("HashMap[" + entry.getKey() + "]: ", entry.getValue(),
          actual.get(entry.getKey()));
  }

  @Test
  public void testNull() throws IllegalAccessException,
      IllegalArgumentException, InstantiationException, Exception,
      NoSuchFieldException {
    assertEquals("N;", PHP.serialize(null));
    assertEquals(null, PHP.unserialize(null, "N;"));
  }

  @Test
  public void testPrimitiveTypes() throws IllegalAccessException,
      IllegalArgumentException, InstantiationException, SecurityException,
      NoSuchFieldException {
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
    assertEquals(tt, PHP.unserialize(String.class, PHP.serialize(tt)));
  }

  @Test
  public void testSerialize() throws IllegalArgumentException,
      IllegalAccessException, InstantiationException, SecurityException,
      NoSuchFieldException {

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
    String[] saa = PHP.unserialize(String[].class, PHP.serialize(sa));
    assertArrayEquals(sa, saa);

    char[] ca = { 'a', 'b' };
    assertEquals("a:2:{i:0;s:1:\"a\";i:1;s:1:\"b\";}", PHP.serialize(ca));
    char[] caa = PHP.unserialize(char[].class, PHP.serialize(ca));
    assertArrayEquals(ca, caa);

    // Многомерные массивы
    int[][] mda = { { 1, 2 }, { 3, 4 } };
    assertEquals("a:2:{i:0;a:2:{i:0;i:1;i:1;i:2;}i:1;a:2:{i:0;i:3;i:1;i:4;}}",
        PHP.serialize(mda));
    int[][] mdaa = PHP.unserialize(mda.getClass(), PHP.serialize(mda));
    assertArrayEquals(mda, mdaa);

    // Хеш-таблицы
    HashMap<String, String> hm = new HashMap<String, String>();
    hm.put("a b", "ss 1");
    hm.put("test", "aa 2");
    assertEquals("a:2:{s:3:\"a b\";s:4:\"ss 1\";s:4:\"test\";s:4:\"aa 2\";}",
        PHP.serialize(hm));
    HSS hma = PHP.unserialize(HSS.class, PHP.serialize(hm));
    assertHashMapEquals(hm, hma);

    // Классы
    MyClass m = new MyClass();
    m.login = 1;
    m.g = 2;
    assertEquals("O:7:\"MyClass\":2:{s:5:\"login\";i:1;s:1:\"g\";i:2;}", PHP
        .serialize(m));
    MyClass ma = PHP.unserialize(MyClass.class, PHP.serialize(m));
    assertEquals(m.login, ma.login);
    assertEquals(m.g, ma.g);

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
    BigClass bca = PHP.unserialize(BigClass.class, PHP.serialize(bc));
    assertEquals(bc.b.length, bca.b.length);
    assertEquals(bc.a.login, bca.a.login);
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

  @Test
  public void type2Class() {
    // Целые типы
    assertEquals(Byte.class, PHP.type2Class(Byte.TYPE));
    assertEquals(Short.class, PHP.type2Class(Short.TYPE));
    assertEquals(Integer.class, PHP.type2Class(Integer.TYPE));
    assertEquals(Long.class, PHP.type2Class(Long.TYPE));
    // Вещественные типы
    assertEquals(Float.class, PHP.type2Class(Float.TYPE));
    assertEquals(Double.class, PHP.type2Class(Double.TYPE));
    // Символы
    assertEquals(Character.class, PHP.type2Class(Character.TYPE));
    // Логический тип
    assertEquals(Boolean.class, PHP.type2Class(Boolean.TYPE));
  }
};
