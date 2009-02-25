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
        //������� �������� �������
        ServerPluginEmulator serverEmulator = new ServerPluginEmulator() {
            //��������� �������� �������. ���� 42 �� "yes", ����� "no". ������ ������� ������
            //����� ��������� ������� ��������� � �������� ����� �� �������
            public HashMap<String, String> checkSolution(HashMap<String, String> solution, HashMap<String, String> previousResult) {
                HashMap<String, String> res = new HashMap<String, String>();
                String ans = solution.get("answer");
                if ("42".equals(ans))
                    res.put("result", "yes");
                else
                    res.put("result", "no");
                return res;
            }

            //���������� ������� � ��������. � ������� ������ ������ �� ������� �� ���������
            //� ������ ������������ ���� � ���� �������
            public File getStatement() {
                return new File("D:\\Data\\Projects\\DCES\\problems\\154\\");
            }
        };

        //������� ���� ��� �������
        PluginBox box = new PluginBox(
                ru.ipo.dces.plugins.Main.class, //����� ���� � ������������ ��������
                serverEmulator, //�������� ������� �������
                "��� ������" //��� ������, �������� ������������
        );

        //������ ���, ����� ����� �������� ���� ��������� �����������
        box.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //���������� ���� �� ������
        box.setVisible(true);
    }

}
