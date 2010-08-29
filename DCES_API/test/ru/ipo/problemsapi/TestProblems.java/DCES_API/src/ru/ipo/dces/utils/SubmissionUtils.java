package ru.ipo.dces.utils;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: I. Posov
 * Date: 09.05.2009
 * Time: 12:08:21
 */
public class SubmissionUtils {

  public static HashMap<String, String> setAttr(String attribute, String value, HashMap<String, String> result) {
    result.put(attribute, value);
    return result;
  }

  public static HashMap<String, String> setAnswer(String value, HashMap<String, String> result) {
    return setAttr("answer", value, result);
  }

  public static HashMap<String, String> setAction(String value, HashMap<String, String> result) {
    return setAttr("action", value, result);
  }

}
