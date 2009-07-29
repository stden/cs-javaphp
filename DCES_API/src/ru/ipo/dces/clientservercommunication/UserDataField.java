package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 23.03.2009
 * Time: 19:35:39
 *
 * Не является запросом или ответом, но используется внутри них. Класс описывает одно поле данных об
 * участнике
 */
public class UserDataField {

  /**
   * Имя поля. Например, "школа" или "класс" или "e-mail"
   */
  public String data;

  /**
   * Обязательно ли поле должно быть заполнено
   */
  public boolean compulsory;

  /**
   * Отображать ли поле в общедоступных результатах соревнования. Например, имя участника отобразить
   * в результатах логично, а его адрес - нет
   */
  public boolean showInResult;

}