package ru.ipo.dces.plugins.admin.beans;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.DateFormat;

/**
 * Created by IntelliJ IDEA.
 * User: Ilya
 * Date: 04.08.2009
 * Time: 16:01:39
 */
public class DateBean {
  private Date day;
  private int hour;
  private int minute;

  private DateFormat formatter = DateFormat.getInstance();

  public DateBean() {
  }

  public DateBean(Date date) {
    setDate(date);
  }

  public Date getDay() {
    return day;
  }

  public void setDay(Date day) {
    this.day = day;
  }

  public int getHour() {
    return hour;
  }

  public void setHour(int hour) {
    this.hour = hour;
  }

  public int getMinute() {
    return minute;
  }

  public void setMinute(int minute) {
    this.minute = minute;
  }

  public Date getDate() {
    Calendar dayC = new GregorianCalendar();
    dayC.setTime(day);

    Calendar c = new GregorianCalendar(
            dayC.get(GregorianCalendar.YEAR),
            dayC.get(GregorianCalendar.MONTH),
            dayC.get(GregorianCalendar.DAY_OF_MONTH),
            hour,
            minute
    );

    return c.getTime();
  }

  public void setDate(Date date) {
    GregorianCalendar c = new GregorianCalendar();
    c.setTime(date);

    day = new GregorianCalendar(c.get(GregorianCalendar.YEAR),
            c.get(GregorianCalendar.MONTH),
            c.get(GregorianCalendar.DAY_OF_MONTH)).getTime();

    hour = c.get(GregorianCalendar.HOUR_OF_DAY);
    minute = c.get(GregorianCalendar.MINUTE);
  }

  @Override
  public String toString() {
    return formatter.format(getDate());
  }
}