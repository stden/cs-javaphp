package ru.ipo.dces.client;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map.Entry;

public class PHP {

  static HashMap<Class<?>, Class<?>> types = new HashMap<Class<?>, Class<?>>();

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
    // Enum
    if (obj instanceof Enum)
      return "s:" + serStr(obj.toString()) + ";";
    // Классы
    if (!obj.getClass().isPrimitive()) {
      Class<?> c = obj.getClass();
      Field[] ff = c.getFields();
      String s = "O:" + serStr(c.getSimpleName()) + ":" + ff.length + ":{";
      for (Field f : ff)
        s += "s:" + serStr(f.getName()) + ";" + serialize(f.get(obj));
      return s + "}";
    }
    throw new IllegalArgumentException("Unknown type " + obj.getClass());
  };

  private static String serStr(String s) {
    return s.length() + ":\"" + s + "\"";
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
