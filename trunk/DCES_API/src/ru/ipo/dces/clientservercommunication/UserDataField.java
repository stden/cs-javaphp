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
  @BinInfo(
          phpDefaultValue = "'name'",
          defaultValue = "Заголовок поля",
          title = "Заголовок"
  )
  public String data;

  /**
   * Обязательно ли поле должно быть заполнено
   */
  @BinInfo(
          phpDefaultValue = "true",
          defaultValue = "false",
          title = "Обязательно заполнить"
  )
  public boolean compulsory;

  /**
   * Отображать ли поле в общедоступных результатах соревнования. Например, имя участника отобразить
   * в результатах логично, а его адрес - нет
   */
  @BinInfo(
          phpDefaultValue="true",
          defaultValue = "true",
          title="Отображать в таблице результатов"
  )
  public boolean showInResult;

}