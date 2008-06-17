package ru.ipo.dces.clientservercommunication;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Илья
 * Date: 21.05.2008
 * Time: 15:09:41
 */
public class ContestDescription implements InfoFrame {
                
    public int contestID;

    //названия доступных контестов
    public String name;

    //описание контеста
    public String description;

    //время начала контеста
    public Date start;

    //время окончания контеста
    public Date finish;

    //свойство контеста - способ регистрации.
    // 0 - можно регистрироваться самому с помощью Клиента
    // 1 - регистриуют только администраторы
    //-1 - значение не установлено
    public int registrationType;

    // 0 - невидимый
    // 1 - видимый
    //-1 - значение не установлено    
    public int visible; //контесты во время настройки не должны быть видимы, видимость можно включать отключать

}
