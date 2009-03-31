package ru.ipo.dces.client;

import ru.ipo.dces.clientservercommunication.*;

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
      res[i] = field.data;
    }
    return res;
  }

}
