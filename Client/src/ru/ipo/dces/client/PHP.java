package ru.ipo.dces.client;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Date;
import java.util.Map.Entry;
import java.nio.charset.Charset;
import java.io.PrintWriter;

public class PHP {

  static HashMap<Class<?>, Class<?>> types = new HashMap<Class<?>, Class<?>>();
  public static final Charset SERVER_CHARSET = Charset.forName("UTF-8");

  static {
    // Целые типы
    types.put(Byte.TYPE, Byte.class);
    types.put(Integer.TYPE, Integer.class);
    types.put(Long.TYPE, Long.class);
    types.put(Short.TYPE, Short.class);
    // Вещественные типы
    types.put(Float.TYPE, Float.class);
    types.put(Double.TYPE, Double.class);
    // Символный тип
    types.put(Character.TYPE, Character.class);
    // Логический тип
    types.put(Boolean.TYPE, Boolean.class);
  }

  private static String getString(StrTokenizer st, char endDelim) {
    int charNum = Integer.parseInt(st.nextToken(':'));
    st.nextToken('"');
    String s = st.nextToken(charNum);
    st.nextToken('"', endDelim);
    return s;
  }

  public static void serialize(Object obj, PrintWriter out) throws IllegalArgumentException, IllegalAccessException {
    if (obj == null)
      out.print("N;");
    // Обработка примитивных типов
    else if (obj instanceof Integer || obj instanceof Long || obj instanceof Byte || obj instanceof Short) {
      out.print("i:");
      out.print(obj);
      out.print(";");
    }
    else if (obj instanceof Double || obj instanceof Float) {
      out.print("d:");
      out.print(obj);
      out.print(";");
    }
    else if (obj instanceof String || obj instanceof Character) {
      out.print("d:");
      serStr(obj.toString(), out);
      out.print(";");
    }
    else if (obj instanceof Boolean) {
      out.print("b:");
      out.print(((Boolean) obj ? "1" : "0"));
      out.print(";");
    }
    // Массивы
    else if (obj.getClass().isArray()) {
      int arrayLen = Array.getLength(obj);
      out.print("a:");
      out.print(arrayLen);
      out.print(":{");
      for (int index = 0; index < arrayLen; index++) {
        serialize(index, out);
        serialize(Array.get(obj, index), out);
      }
      out.print("}");
    }
    // Ассоциативные массивы
    else if (obj instanceof HashMap<?, ?>) {
      HashMap<?, ?> hm = (HashMap<?, ?>) obj;
      out.print("a:");
      out.print(hm.size());
      out.print(":{");
      for (Entry<?, ?> entry : hm.entrySet()) {
        serialize(entry.getKey(), out);
        serialize(entry.getValue(), out);
      }
      out.print("}");
    }
    // Enum
    else if (obj instanceof Enum) {
      out.print("s:");
      serStr(obj.toString(), out);
      out.print(";");
    }
    // Классы
    else if (!obj.getClass().isPrimitive()) {
      Class<?> c = obj.getClass();
      Field[] ff = c.getFields();
      out.print("O:");
      serStr(c.getSimpleName(), out);
      out.print(":");
      out.print(ff.length);
      out.print(":{");
      for (Field f : ff) {
        out.print("s:");
        serStr(f.getName(), out);
        out.print(";");
        serialize(f.get(obj), out);
      }
      out.print("}");
    }
    else throw new IllegalArgumentException("Unknown type " + obj.getClass());
  }

  private static void serStr(String s, PrintWriter out) {
    out.print(s.getBytes(SERVER_CHARSET).length);
    out.print(":\"");
    out.print(s);
    out.print("\"");            
  }

  public static Class<?> type2Class(Class<?> type) {
    Class<?> cls = types.get(type);
    return cls != null ? cls : type;
  }

  public static <T> T unserialize(Class<T> cls, String s)
      throws IllegalArgumentException, InstantiationException,
      IllegalAccessException, SecurityException, NoSuchFieldException,
      IllegalClassException {
    return unserialize(cls, new StrTokenizer(s));
  }

  @SuppressWarnings("unchecked")
  static <T> T unserialize(Class<T> cls, StrTokenizer st)
      throws InstantiationException, IllegalAccessException, SecurityException,
      NoSuchFieldException, IllegalClassException {
    String nextToken = st.nextToken(':');
    switch (nextToken.charAt(0)) {
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
        if (cls == Date.class)
          return cls.cast(new Date(Long.parseLong(intNum) / 1000));
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
        String s = getString(st, ';');
        if (cls == Character.class) {
          if (s.length() != 1)
            throw new IllegalArgumentException("Lenght must be 1!");
          return cls.cast(s.charAt(0));
        }
        if (cls == String.class)
          return cls.cast(s);
        if (cls.isEnum())
          return (T) Enum.valueOf((Class<Enum>) cls, s);
        throw new IllegalArgumentException(" \"" + s + "\"  string != "
            + cls.getCanonicalName());
      case 'a':
        int length = Integer.parseInt(st.nextToken(':'));
        st.nextToken('{');
        if (cls.isArray()) {
          Object array = Array.newInstance(cls.getComponentType(), length);
          for (int i = 0; i < length; i++) {
            int index = unserialize(Integer.class, st);
            Array.set(array, index, unserialize(type2Class(cls
                .getComponentType()), st));
          }
          st.nextToken('}');
          return cls.cast(array);
        }
        if (cls.asSubclass(HashMap.class) != null) {
          ParameterizedType t = (ParameterizedType) cls.getGenericSuperclass();
          Type[] params = t.getActualTypeArguments();
          Class K = (Class) params[0];
          Class V = (Class) params[1];
          HashMap hm = (HashMap) cls.newInstance();
          for (int i = 0; i < length; i++)
            hm.put(unserialize(K, st), unserialize(V, st));
          st.nextToken('}');
          return cls.cast(hm);
        }
      case 'O':
        // Можно игнорировать имя класса, тогда можно будет десериалировать в
        // потомка
        String className = getString(st, ':');
        if (className.compareTo(cls.getSimpleName()) != 0)
          throw new IllegalClassException(cls.getSimpleName(), className);
        Object obj = cls.newInstance(); // Создаём объект
        int fieldsNum = Integer.parseInt(st.nextToken(':'));
        st.nextToken('{');
        for (int i = 0; i < fieldsNum; i++) { // Заполняем поля
          String fieldName = unserialize(String.class, st);
          Field f = cls.getField(fieldName);
          f.set(obj, unserialize(type2Class(f.getType()), st));
        }
        st.nextToken('}');
        return cls.cast(obj);
      default:
        break;
    }
    throw new IllegalArgumentException(cls.getCanonicalName() + " "
        + st.toString());
  }
}
