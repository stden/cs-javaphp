package ru.ipo.dces.exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: Илья
 * Date: 30.10.2008
 * Time: 1:30:38
 */
public class ServerReturnedError extends Exception {

  private int errNo;
  private String extendedInfo;

  public int getErrNo() {
    return errNo;
  }

  public String getExtendedInfo() {
    return extendedInfo;
  }

  public ServerReturnedError(int errNo, String extendedInfo) {
    super(getErrorMessage(errNo));
    this.errNo = errNo;
    this.extendedInfo = extendedInfo;
  }

  private static String getErrorMessage(int errNo) {
    switch (errNo) {
      case 0: return "Недостаточно прав для совершения операции";
      case 1: return "Указано недостаточно параметров для совершения операции";
      case 2: return "Не удается найти указанного пользователя";
      case 3: return "Неверно указана сессия";
      case 4: return "Не удается найти указанную задачу";
      case 5: return "Не удается найти указанный серверный плагин";
      case 6: return "Не удается найти указанный клиентский плагин";
      case 7: return "Данные с условием не являются zip файлом";
      case 8: return "Данные с ответом не являются zip файлом";
      case 9: return "Серверный плагин не принял данные с условием";
      case 10: return "Серверный плагин не принял данные с ответом";
      case 11: return "Запрос не произвел изменений";
      case 12: return "Контест, логин или пароль указаны неверно";
      case 13: return "База данных уже существует";
      case 14: return "Не удается найти указанный контест";
      case 15: return "Не удается разобрать запрос";
      case 16: return "Нельзя удалять нулевой контест";
      case 17: return "Пользователь с данным логином уже зарегистрирован";
      case 18: return "Суперадминистратор может быть зарегитрирован только для нулевого контеста";
      default: return "Неизвестная ошибка";
    }
  }
}
