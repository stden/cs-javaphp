package ru.ipo.dces.client;

import ru.ipo.dces.clientservercommunication.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Field;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 31.03.2009
 * Time: 17:34:59
 */
public class RequestResponseUtils {

    public static String[] extractFieldNames(UserDataField[] data) {
        String[] res = new String[data.length];
        for (int i = 0; i < data.length; i++) {
            UserDataField field = data[i];
            res[i] = field == null ? "" : field.data;
        }
        return res;
    }

    public static <T> T copyValue(T initial, Class<T> clazz) {
        try {
            if (initial == null)
                return initial;
            else if (clazz.isPrimitive())
                return initial;
            else if (clazz == Boolean.class || clazz == Character.class || clazz == Byte.class ||
                    clazz == Short.class || clazz == Integer.class || clazz == Long.class || clazz == Float.class ||
                    clazz == Double.class || clazz == Void.class)
                return initial;
            else if (clazz == String.class) {
                return initial;
            } else if (clazz.isEnum()) {
                return initial;
            } else if (clazz.isArray()) {
                Class type = clazz.getComponentType();
                int length = Array.getLength(initial);
                if (type.isPrimitive())
                    return (T) Arrays.copyOf((Object[]) initial, length);

                Object res = Array.newInstance(type, length);
                for (int i = 0; i < length; i++)
                    Array.set(res, i, copyValue(Array.get(initial, i), type));
                return (T) res;
            } else if (clazz == Date.class)
                return (T) new Date(((Date) initial).getTime());
            else /*structure type*/ {
                T res;
                Constructor<T> c = clazz.getConstructor();
                res = c.newInstance();

                for (Field field : clazz.getFields())
                    field.set(res, copyValue(field.get(initial), (Class) field.getType()));

                return res;
            }
        } catch (Exception e) {
            throw new RuntimeException("failed to copy structure data");
        }
    }

}
