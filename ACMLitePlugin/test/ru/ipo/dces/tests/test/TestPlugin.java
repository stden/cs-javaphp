package ru.ipo.dces.tests.test;

import ru.ipo.dces.debug.PluginBox;
import ru.ipo.dces.debug.ServerPluginProxy;
import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.server.http.HttpServer;
import ru.ipo.dces.exceptions.ServerReturnedError;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;
import ru.ipo.dces.plugins.ACMLitePlugin;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: 30.01.2009
 * Time: 19:18:30
 */

public class TestPlugin {

  //URL сервера
  private static final String SERVER_URL = "http://vm-2.spb.ru/~posov/dces/dces.php";

  //логин и пароль суперадминистратора
  public static final String LOGIN = "admin";
  public static final String PASSWORD = "pass";
  
  //номер соревнования, с помощью которого будет происходить отладка  
  public static final int CONTEST_ID = 50;

  //ID отлаживаемого плагина клиента и сервер. Придумывается автором плагина
  public static final String CLIENT_PLUGIN_ALIAS = "ACMLitePlugin2";
  public static final String SERVER_PLUGIN_ALIAS = "MyDebugPlugin2";
  
  //путь к отлаживаемым плагинам на диске
  public static final String CLIENT_PLUGIN_PATH = "d:\\programming\\DCES\\plugins\\ACMLitePlugin.jar";
  public static final String SERVER_PLUGIN_PATH = "d:\\programming\\DCES\\ACMLitePlugin\\debug\\MyDebugPlugin2.php";
                                                                                                            
  //каталог, в который при отладке будет положено скаченное с сервера условие
  public static final String DEBUG_STATEMENT_FOLDER = "C:/programming/dces/debug-problem-folder/";
  //класс отлаживаемого плагина клиента
  public static final Class<? extends Plugin> PLUGIN_CLASS = ACMLitePlugin.class;

  //Имя задачи, отображается при отладке, особого значения не имеет
  public static final String PROBLEM_NAME = "Имя задачи";
  //пути к каталогам или файлам с условием и ответом
  private static final String STATEMENT_PATH = "d:\\programming\\DCES\\ACMLitePlugin\\debug\\1\\task";
  private static final String ANSWER_PATH = "d:\\programming\\DCES\\ACMLitePlugin\\debug\\1\\answer\\01.a";

  public static void main(String[] args) throws ServerReturnedError, GeneralRequestFailureException, IOException {
    //создаем объект, соответствующий реальному серверу
    HttpServer server = new HttpServer(SERVER_URL);

    //входим в систему от имени суперадминистратора
    ServerPluginProxy proxy = new ServerPluginProxy(server, LOGIN, PASSWORD, true);
    //выбираем соревнование, внутри которого будет происходить тестирование
    proxy.selectContest(CONTEST_ID);
    //Загрузить плагин клиента на сервер
    proxy.uploadClientPlugin(CLIENT_PLUGIN_ALIAS, new File(CLIENT_PLUGIN_PATH));
    //Загрузить отлаживаемый плагин на сервер
    proxy.uploadServerPlugin(SERVER_PLUGIN_ALIAS, new File(SERVER_PLUGIN_PATH));
    proxy.setStatementFolder(new File(DEBUG_STATEMENT_FOLDER));
    proxy.createProblem(
            CLIENT_PLUGIN_ALIAS,
            SERVER_PLUGIN_ALIAS,
            new File(STATEMENT_PATH),
            new File(ANSWER_PATH)
    );
    //создать участника, от имени которого будет вестись отладка
    proxy.newParticipant();

    //создаем окно для отладки
    PluginBox box = new PluginBox(
            PLUGIN_CLASS, //класс файл с отлаживаемым плагином
            proxy, //эмулятор плагина сервера
            PROBLEM_NAME //имя задачи, параметр необязателен
    );

    //делаем так, чтобы после закрытия окна программа закрывалась
    box.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //показываем окно на экране
    box.setVisible(true);
  }

}


