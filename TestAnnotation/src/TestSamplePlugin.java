import ru.ipo.dces.debug.PluginBox;
import ru.ipo.dces.debug.ServerPluginEmulator;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: 30.01.2009
 * Time: 19:18:30
 */
public class TestSamplePlugin {

    public static void main(String[] args) {
        //создаем эмуляцию сервера
        ServerPluginEmulator serverEmulator = new ServerPluginEmulator() {
            //Реализуем проверку решения. Если 42 то "yes", иначе "no". Плагин клиента должен
            //уметь оформлять решение участника и понимать ответ от сервера
            public HashMap<String, String> checkSolution(HashMap<String, String> solution, HashMap<String, String> previousResult) {
                HashMap<String, String> res = new HashMap<String, String>();
                String ans = solution.get("answer");
                if ("42".equals(ans))
                    res.put("result", "yes");
                else
                    res.put("result", "no");
                return res;
            }

            //Возвращаем каталог с задачами. В простом случае задача не зависит от участника
            //и всегда возвращается одно и тоже условие
            public File getStatement() {
                return new File("D:\\Data\\Projects\\DCES\\problems\\154\\");
            }
        };

        //создаем окно для отладки
        PluginBox box = new PluginBox(
                ru.ipo.dces.plugins.Main.class, //класс файл с отлаживаемым плагином
                serverEmulator, //эмулятор плагина сервера
                "Имя Задачи" //имя задачи, параметр необязателен
        );

        //делаем так, чтобы после закрытия окна программа закрывалась
        box.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //показываем окно на экране
        box.setVisible(true);
    }

}
