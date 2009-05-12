package ru.ipo.dces.server.http;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Date;
import java.util.Map.Entry;
import java.nio.charset.Charset;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;

class PHP {

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

  private static String getString(StreamTokenizer st, char endDelim) throws IOException {
    int bytesNum = Integer.parseInt(st.readToken(':'));
    st.moveTo('"');
    String s = new String(st.readBytes(bytesNum), SERVER_CHARSET);
    st.moveTo('"');
    st.moveTo(endDelim);
    return s;
  }

  public static void serialize(Object obj, OutputStream out) throws IllegalArgumentException, IllegalAccessException, IOException {
    if (obj == null)
    {
      out.write('N');
      out.write(';');
    }
    // Обработка примитивных типов
    else if (obj instanceof Integer || obj instanceof Long || obj instanceof Byte || obj instanceof Short) {
      out.write('i');
      out.write(':');
      serObjToString(obj, out);
      out.write(';');
    }
    // Дата и время
    else if (obj instanceof Date) {
      out.write('i');
      out.write(':');
      serObjToString(((Date)obj).getTime() / 1000, out);
      out.write(';');
    }
    //вещественные
    else if (obj instanceof Double || obj instanceof Float) {
      out.write('d');
      out.write(':');
      serObjToString(obj, out);
      out.write(';');
    }
    //символьные типы
    else if (obj instanceof String || obj instanceof Character) {
      out.write('s');
      out.write(':');
      serStr(obj.toString(), out);
      out.write(';');
    }
    //логические
    else if (obj instanceof Boolean) {
      out.write('b');
      out.write(':');
      out.write((Boolean) obj ? '1' : '0');
      out.write(';');
    }
    // Массив байт
    else if (obj.getClass().isArray() && obj.getClass().getComponentType() == byte.class) {
      out.write('s');
      out.write(':');

      serObjToString(Array.getLength(obj), out);
      out.write(':');
      out.write('"');
      out.write((byte[])obj);
      out.write('"');

      out.write(';');
    }
    // Остальные массивы
    else if (obj.getClass().isArray()) {
      int arrayLen = Array.getLength(obj);
      out.write('a');
      out.write(':');
      serObjToString(arrayLen, out);
      out.write(':');
      out.write('{');
      for (int index = 0; index < arrayLen; index++) {
        serialize(index, out);
        serialize(Array.get(obj, index), out);
      }
      out.write('}');
    }
    // Ассоциативные массивы
    else if (obj instanceof HashMap<?, ?>) {
      HashMap<?, ?> hm = (HashMap<?, ?>) obj;
      out.write('a');
      out.write(':');
      serObjToString(hm.size(), out);
      out.write(':');
      out.write('{');
      for (Entry<?, ?> entry : hm.entrySet()) {
        serialize(entry.getKey(), out);
        serialize(entry.getValue(), out);
      }
      out.write('}');
    }
    // Enum
    else if (obj instanceof Enum) {
      out.write('s');
      out.write(':');
      serStr(obj.toString(), out);
      out.write(';');
    }
    // Классы
    else if (!obj.getClass().isPrimitive()) {
      Class<?> c = obj.getClass();
      Field[] ff = c.getFields();
      out.write('O');
      out.write(':');
      serStr(c.getSimpleName(), out);
      out.write(':');
      serObjToString(ff.length, out);
      out.write(':');
      out.write('{');
      for (Field f : ff) {
        out.write('s');
        out.write(':');
        serStr(f.getName(), out);
        out.write(';');
        serialize(f.get(obj), out);
      }
      out.write('}');
    }
    else throw new IllegalArgumentException("Unknown type " + obj.getClass());
  }

  private static void serObjToString(Object i, OutputStream out) throws IOException {
    out.write(i.toString().getBytes(SERVER_CHARSET));
  }

  private static void serStr(String s, OutputStream out) throws IOException {
    final byte[] strBytes = s.getBytes(SERVER_CHARSET);
    serObjToString(strBytes.length, out);
    out.write(':');
    out.write('"');
    out.write(strBytes);
    out.write('"');
  }

  public static Class<?> type2Class(Class<?> type) {
    Class<?> cls = types.get(type);
    return cls != null ? cls : type;
  }

  public static <T> T unserialize(Class<T> cls, InputStream in)
          throws IllegalArgumentException, InstantiationException,
          IllegalAccessException, SecurityException, NoSuchFieldException,
          IllegalClassException, IOException {
    return unserialize(cls, new StreamTokenizer(in));
  }

  @SuppressWarnings("unchecked")
  static <T> T unserialize(Class<T> cls, StreamTokenizer st)
          throws InstantiationException, IllegalAccessException, SecurityException,
          NoSuchFieldException, IllegalClassException, IOException {
    String nextToken = st.readToken(':',';');    

    //TODO think of 'charAt()'. The string should have only one symbol
    switch (nextToken.charAt(0)) {
      case 'N':
        return null;
      case 'i':
        String intNum = st.readToken(';');
        if (cls == Byte.class)
          return cls.cast(Byte.parseByte(intNum));
        if (cls == Short.class)
          return cls.cast(Short.parseShort(intNum));
        if (cls == Integer.class)
          return cls.cast(Integer.parseInt(intNum));
        if (cls == Long.class)
          return cls.cast(Long.parseLong(intNum));
        if (cls == Date.class)
          return cls.cast(new Date(Long.parseLong(intNum) * 1000));
        if (cls == String.class)
          return cls.cast(intNum);
        throw new IllegalArgumentException("Expected integer type, found: "
            + cls.getCanonicalName());
      case 'd':
        String floatNum = st.readToken(';');
        if (cls == Float.class)
          return cls.cast(Float.parseFloat(floatNum));
        if (cls == Double.class)
          return cls.cast(Double.parseDouble(floatNum));
        throw new IllegalArgumentException("Expected float type, found: "
            + cls.getCanonicalName());
      case 'b':
        switch (Integer.parseInt(st.readToken(';'))) {
          case 0:
            try {
              return cls.cast(false);
            } catch (Exception e) {
              //boolean sometimes mean null
              System.out.println("Warning: false value in deserialization might mean null");
              return null;
            }
          case 1:
            return cls.cast(true);
          default:
            throw new IllegalArgumentException("Expected 0 or 1");
        }
      case 's':
        if (cls == Character.class)
        {
          String s = getString(st, ';');
          if (s.length() != 1)
            throw new IllegalArgumentException("Lenght must be 1!");
          return cls.cast(s.charAt(0));
        }
        else if (cls == String.class)
        {
          String s = getString(st, ';');
          return cls.cast(s);
        }
        else if (cls.isEnum())
        {
          String s = getString(st, ';');
          return (T) Enum.valueOf((Class<Enum>) cls, s);
        }
        else if (cls.isArray() && cls.getComponentType() == byte.class) //array of bytes
        {
          int bytesNum = Integer.parseInt(st.readToken(':'));
          st.moveTo('"');
          byte[] res = st.readBytes(bytesNum);
          st.moveTo('"');
          st.moveTo(';');
          return cls.cast(res);
        }
        else
        {
          String s = getString(st, ';');
          throw new IllegalArgumentException(" \"" + s + "\"  string != "
            + cls.getCanonicalName());
        }
      case 'a':      
        int length = Integer.parseInt(st.readToken(':'));
        st.moveTo('{');
        if (cls.isArray()) {
          Object array = Array.newInstance(cls.getComponentType(), length);
          for (int i = 0; i < length; i++) {
            int index = unserialize(Integer.class, st);
            Array.set(array, index, unserialize(type2Class(cls
                .getComponentType()), st));
          }
          st.moveTo('}');
          return cls.cast(array);
        }
        //if (cls.asSubclass(HashMap.class) != null) {
        if (cls == HashMap.class) {
          //ParameterizedType t = (ParameterizedType) cls.getGenericSuperclass();
          //Type[] params = t.getActualTypeArguments();
          //TODO to think a lot. Generics are not saved in Runtime, and we can not get real values for K and V
          Class K = String.class; //(Class) params[0];
          Class V = String.class;  //(Class) params[1];
          HashMap hm = (HashMap) cls.newInstance();
          for (int i = 0; i < length; i++)
            hm.put(unserialize(K, st), unserialize(V, st));
          st.moveTo('}');
          return cls.cast(hm);
        }
      case 'O':
        // Можно игнорировать имя класса, тогда можно будет десериалировать в
        // потомка
        String className = getString(st, ':');
        if (className.compareTo(cls.getSimpleName()) != 0)
          throw new IllegalClassException(cls.getSimpleName(), className);
        Object obj = cls.newInstance(); // Создаём объект
        int fieldsNum = Integer.parseInt(st.readToken(':'));
        st.moveTo('{');
        for (int i = 0; i < fieldsNum; i++) { // Заполняем поля
          String fieldName = unserialize(String.class, st);
          Field f = cls.getField(fieldName);
          f.set(obj, unserialize(type2Class(f.getType()), st));
        }
        st.moveTo('}');
        return cls.cast(obj);
      default:
        break;
    }
    throw new IllegalArgumentException(cls.getCanonicalName() + " " + st.toString());
  }
}
