package ru.ipo.dces.tests.test;

import ru.ipo.dces.debug.PluginBox;
import ru.ipo.dces.debug.ServerPluginProxy;
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

  public static void main(String[] args) throws ServerReturnedError, GeneralRequestFailureException, IOException {
   // HttpServer server = new HttpServer("http://dces-server.ru:423/dces.php");
    HttpServer server = new HttpServer("http://ipo.spb.ru/dces/test/dces.php");

    ServerPluginProxy proxy = new ServerPluginProxy(server, "admin", "pass", true);
    proxy.selectContest(1);
    proxy.uploadServerPlugin("FirstDebugPlugin", new File("C://dces/ACMLiteCheckerPlugin.php"));
    proxy.setStatementFolder(new File("C://dces/debug-problem-folder/"));
    proxy.createProblem("SamplePlugin", "FirstDebugPlugin", new File("C://dces/problem_folder/1/task/"), new File("C://dces/problem_folder/1/answer/01.a"));
    proxy.newParticipant();

    //создаем окно для отладки
    PluginBox box = new PluginBox(
            ACMLitePlugin.class, //класс файл с отлаживаемым плагином
            //clientPlugin,
            proxy, //эмулятор плагина сервера
            "Имя Задачи" //имя задачи, параметр необязателен
    );

    //делаем так, чтобы после закрытия окна программа закрывалась
    box.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //показываем окно на экране
    box.setVisible(true);
  }

}


