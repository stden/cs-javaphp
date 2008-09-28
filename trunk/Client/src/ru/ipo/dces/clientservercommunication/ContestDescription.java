package ru.ipo.dces.clientservercommunication;

import java.util.Date;

/**
 * Описание контеста
 */
public class ContestDescription {

  public static enum RegistartionType {
    /** можно регистрироваться самому с помощью Клиента */
    Self,
    /** регистриуют только администраторы */
    ByAdmins,
    /** значение не установлено */
    NotDefined
  }

  /** ID контеста */
  public int              contestID;

  /** название контеста */
  public String           name;

  /** описание контеста */
  public String           description;

  /** время начала контеста */
  public Date             start;

  /** время окончания контеста */
  public Date             finish;

  /** способ регистрации на контест */
  public RegistartionType registrationType;

  /**
   * контесты во время настройки не должны быть видимы, видимость можно
   * включать/отключать
   */
  public boolean          visible = true;

  /**
   * названия данных, которые должны быть у каждого участника контеста.
   * Например, {'Имя','Фамилия','Школа','Класс'}
   */
  public String[]         data;

  public ContestDescription() {
  };

  public ContestDescription(String name) {
    this.name = name;
  }

}
