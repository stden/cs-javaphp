package Client;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map.Entry;

public class PHP {

  static HashMap<Class<?>, Class<?>> types = new HashMap<Class<?>, Class<?>>();

  static {
    types.put(Byte.TYPE, Byte.class);
    types.put(Integer.TYPE, Integer.class);
    types.put(Long.TYPE, Long.class);
    types.put(Short.TYPE, Short.class);
    types.put(Float.TYPE, Float.class);
    types.put(Double.TYPE, Double.class);
    types.put(Character.TYPE, Character.class);
    types.put(Boolean.TYPE, Boolean.class);
  }

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
  };

  public static Class<?> type2Class(Class<?> type) {
    Class<?> cls = types.get(type);
    return cls != null ? cls : type;
  }

  public static <T> T unserialize(Class<T> cls, String s)
      throws IllegalArgumentException, InstantiationException,
      IllegalAccessException {
    return unserialize(cls, new StrTokenizer(s));
  }

  static <T> T unserialize(Class<T> cls, StrTokenizer st) {
    switch (st.nextToken(':').charAt(0)) {
      case 'N':
        return null;
      case 'i':
        String intNum = st.nextToken(';');
        if (cls == Byte.class)
          return cls.cast(Byte.parseByte(intNum));
        if (cls == Short.class)
          return cls.cast(Short.parseShort(intNum));
        if (cls == Integer.class)
          return cls.cast(Integer.parseInt(intNum));
        if (cls == Long.class)
          return cls.cast(Long.parseLong(intNum));
        throw new IllegalArgumentException("Expected integer type, found: "
            + cls.getCanonicalName());
      case 'd':
        String floatNum = st.nextToken(';');
        if (cls == Float.class)
          return cls.cast(Float.parseFloat(floatNum));
        if (cls == Double.class)
          return cls.cast(Double.parseDouble(floatNum));
        throw new IllegalArgumentException("Expected float type, found: "
            + cls.getCanonicalName());
      case 'b':
        switch (Integer.parseInt(st.nextToken(';'))) {
          case 0:
            return cls.cast(false);
          case 1:
            return cls.cast(true);
          default:
            throw new IllegalArgumentException("Expected 0 or 1");
        }
      case 's':
        int charNum = Integer.parseInt(st.nextToken(':'));
        st.nextToken('"');
        String s = st.nextToken(charNum);
        st.nextToken(';');
        if (cls == Character.class) {
          if (charNum != 1)
            throw new IllegalArgumentException("Lenght must be 1!");
          return cls.cast(s.charAt(0));
        }
        if (cls == String.class)
          return cls.cast(s);
        throw new IllegalArgumentException("Expected string type, found: "
            + cls.getCanonicalName());
      case 'a':
        int length = Integer.parseInt(st.nextToken(':'));
        Object array = Array.newInstance(cls.getComponentType(), length);
        st.nextToken('{');
        for (int i = 0; i < length; i++) {
          int index = unserialize(Integer.class, st);
          Array.set(array, index, unserialize(
              type2Class(cls.getComponentType()), st));
        }
        st.nextToken('}');
        return cls.cast(array);
      default:
        break;
    }
    throw new IllegalArgumentException("!! " + cls.getCanonicalName() + " "
        + st.toString());
  }

}
