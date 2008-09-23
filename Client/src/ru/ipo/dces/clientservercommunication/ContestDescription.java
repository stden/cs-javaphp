package ru.ipo.dces.clientservercommunication;

import java.util.Date;

/**
 * Описание контеста
 */
public class ContestDescription {
  //TODO: подумать, не заменить ли int на Integer, чтобы задавать дефолтовое значение через null, а не -1 

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

  //названия данных, которые должны быть у каждого участника контеста. Например, {'Имя','Фамилия','Школа','Класс'} 
  public String[] data;

  public ContestDescription(String name) {
    this.name = name;
  }

}
