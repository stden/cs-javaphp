package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: Илья
 * Date: 21.05.2008
 * Time: 15:20:38
 */
public class ProblemDescription implements InfoFrame {

    public String id; //идентификатор задачи
    public String clientPluginID; //ID плагина, который будет обрабатывать задачу
    public String serverPluginID; //ID плагина, который стороны сервера, получает результаты по задаче
    public String name; //название
    public byte[] problemData; //условие задачи. Вероятнее всего, это архив, который будет автоматически раскрываться в каталог, соответствующий задаче

}
