package ru.ipo.dces.tests.plugins;

import ru.ipo.dces.debug.PluginBox;
import ru.ipo.dces.debug.ServerPluginProxy;
import ru.ipo.dces.server.http.HttpServer;
import ru.ipo.dces.exceptions.ServerReturnedError;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;
import ru.ipo.dces.pluginapi.Plugin;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: 30.01.2009
 * Time: 19:18:30
 */

public class TestSamplePlugin {

  public static void main(String[] args) throws ServerReturnedError, GeneralRequestFailureException, IOException {
    HttpServer server = new HttpServer("http://dces-server.ru:423/dces.php");

    ServerPluginProxy proxy = new ServerPluginProxy(server, "admin", "pass", true);
    proxy.selectContest(1);
    proxy.uploadServerPlugin("FirstDebugPlugin", new File("debug/debugPlugin.php"));
    proxy.setStatementFolder(new File("debug/debug-problem-folder"));
    //Class<? extends Plugin> clientPlugin = proxy.getClientPlugin("SamplePlugin");
    proxy.createProblem("SamplePlugin", "FirstDebugPlugin", new File("problemsData/1/statement.html"), new File("problemsData/1/answer.txt"));
    proxy.newParticipant();

    //создаем окно для отладки
    PluginBox box = new PluginBox(
            ru.ipo.dces.plugins.Main.class, //класс файл с отлаживаемым плагином
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