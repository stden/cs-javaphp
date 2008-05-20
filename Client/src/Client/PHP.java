package Client;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map.Entry;

public class PHP {

  public static String serialize(Object obj) throws IllegalArgumentException,
      IllegalAccessException {
    if (obj == null)
      return "N;";
    // Обработка примитивных типов
    if (obj instanceof Integer || obj instanceof Long || obj instanceof Byte
        || obj instanceof Short)
      return "i:" + obj + ";";
    if (obj instanceof Double || obj instanceof Float)
      return "d:" + obj + ";";
    if (obj instanceof String || obj instanceof Character)
      return "s:" + serStr(obj.toString()) + ";";
    if (obj instanceof Boolean)
      return "b:" + ((Boolean) obj ? "1" : "0") + ";";
    // Массивы
    if (obj.getClass().isArray()) {
      int arrayLen = Array.getLength(obj);
      String s = "a:" + arrayLen + ":{";
      for (int index = 0; index < arrayLen; index++)
        s += serialize(index) + serialize(Array.get(obj, index));
      return s + "}";
    }
    // Ассоциативные массивы
    if (obj instanceof HashMap<?, ?>) {
      HashMap<?, ?> hm = (HashMap<?, ?>) obj;
      String s = "a:" + hm.size() + ":{";
      for (Entry<?, ?> entry : hm.entrySet())
        s += serialize(entry.getKey()) + serialize(entry.getValue());
      return s + "}";
    }
    // Классы
    if (!obj.getClass().isPrimitive()) {
      Class<?> c = obj.getClass();
      Field[] ff = c.getFields();
      String s = "O:" + serStr(c.getSimpleName()) + ":" + ff.length + ":{";
      for (Field f : ff)
        s += "s:" + serStr(f.getName()) + ";" + serialize(f.get(obj));
      return s + "}";
    }
    return "Незнакомый тип!";
  }

  private static String serStr(String s) {
    return s.length() + ":\"" + s + "\"";
  }

  public static Object unserialize(String s) {
    switch (s.charAt(0)) {
      case 'N':
        return null;
      case 'i':
        return Long.parseLong(s.substring(2, s.indexOf(';')));
      case 'd':
        return Double.parseDouble(s.substring(2, s.indexOf(';')));
      case 'b':
        return s.charAt(2) == '1';
      default:
        break;
    }
    return 200;
  }

}
