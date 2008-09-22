package ru.ipo.dces.clientservercommunication;

import java.util.Date;

/**
 * Описание контеста
 */
public class ContestDescription {

  public int    contestID;

  // названия доступных контестов
  public String name;

  // описание контеста
  public String description;

  // время начала контеста
  public Date   start;

  // время окончания контеста
  public Date   finish;

  // свойство контеста - способ регистрации.
  // 0 - можно регистрироваться самому с помощью Клиента
  // 1 - регистриуют только администраторы
  // -1 - значение не установлено
  public int    registrationType;

  // 0 - невидимый
  // 1 - видимый
  // -1 - значение не установлено
  public int    visible;         // контесты во время настройки не должны быть

  // видимы, видимость можно включать отключать

  public ContestDescription(String name) {
    this.name = name;
  }

}
