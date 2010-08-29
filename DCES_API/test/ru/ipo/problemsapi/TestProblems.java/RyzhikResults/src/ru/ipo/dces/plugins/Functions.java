package ru.ipo.dces.plugins;

/**
 * Created by IntelliJ IDEA.
 * User: Поля
 * Date: 17.05.2009
 * Time: 2:00:13
 * To change this template use File | Settings | File Templates.
 */
public class Functions {

  public static int getSecText(int w, int r, int d) {
    int t = 0;
    if (w == 10) t = 1;
    else {
      if (r == 10) t = 2;
      else {
        if (d == 10) t = 3;
        else {
          if (w > 2 & w < 10) {
            if (r >= 7) t = 4;
            else {
              if (d >= 2) t = 5;
              else t = 6;
            }
          }
          if (w == 0) {
            if (r >= 7) t = 7;
            else t = 8;
          }
          if (w > 0 & w <= 2) {
            if (r >= 7) t = 9;
            else t = 10;
          }
        }
      }
    }
    return t;
  }

  public static int getParamText(float e, float p) {
    int t;
    if (e == 100) t = 1;
    else {
      if (e >= 85) t = 2;
      else {
        if (e >= 70) {
          if (p >= 50) t = 3;
          else t = 4;
        } else {
          if (e >= 55) {
            if (p >= 50) t = 5;
            else t = 6;
          } else {
            if (e >= 40) t = 7;
            else t = 8;
          }
        }
      }
    }
    return t;

  }

  public static int getText(int w, int r, int d) {
    int t = 0;
    if (w == 30) t = 1;
    else {
      if (r == 30) t = 2;
      else {
        if (d == 30) t = 3;
        else {
          if (r <= w) t = 1;
          else {
            if (w > 6 & w < 30) {
              if (r >= 21) t = 4;
              else {

                if (d >= 6) t = 5;
                else t = 6;
              }
            }
            if (w == 0) {
              if (r >= 21) t = 7;
              else t = 8;
            }
            if (w > 0 & w <= 6) {
              if (r >= 21) t = 9;
              else t = 10;
            }
          }
        }
      }
    }
    return t;
  }
}
