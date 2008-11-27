package ru.ipo.dces.plugins.admin;

import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.pluginapi.PluginEnvironment;

import javax.swing.*;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

/**
 * Created by IntelliJ IDEA.
 * User: Michael Kazekin
 * Date: 11.11.2008
 * Time: 20:13:25
 * To change this template use File | Settings | File Templates.
 */
public class ManageContestsPlugin extends Plugin {
    private JPanel panel1;
    private JList list1;
    private JTextField contestName;
    private JFormattedTextField beginTime;
    private JFormattedTextField beginDate;
    private JFormattedTextField endDate;
    private JFormattedTextField endTime;
    private JTextArea contestDescription;
    private JRadioButton ownRegistrationRB;
    private JRadioButton administratorRegistrationRB;
    private JTextField problemName;
    private JTextField problemStatement;
    private JTextField clientPlugin;
    private JButton changeStatementButton;
    private JTextField serverPlugin;
    private JTextField problemAnswer;
    private JButton changeAnswerButton;

    /**
     * ������������� plugin'�
     */
    public ManageContestsPlugin(PluginEnvironment env) {
        super(env);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:122px:noGrow,left:4dlu:noGrow,fill:2dlu:noGrow,left:4dlu:noGrow,fill:92dlu:noGrow,left:4dlu:noGrow,fill:82dlu:noGrow,left:4dlu:noGrow,fill:81dlu:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:60dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:6dlu:noGrow,center:28px:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        list1 = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        defaultListModel1.addElement("� �������������� �������� �� ������ ���� ���� �����, ������ ��� �� ����������� ������ ����(!!!) �������");
        list1.setModel(defaultListModel1);
        CellConstraints cc = new CellConstraints();
        panel1.add(list1, cc.xywh(3, 5, 1, 7, CellConstraints.DEFAULT, CellConstraints.FILL));
        contestName = new JTextField();
        panel1.add(contestName, cc.xyw(9, 3, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label1 = new JLabel();
        label1.setText("�������� ��������");
        panel1.add(label1, cc.xy(7, 3));
        final JLabel label2 = new JLabel();
        label2.setText("���� � ����� ������");
        panel1.add(label2, cc.xy(7, 5));
        beginTime = new JFormattedTextField();
        panel1.add(beginTime, cc.xy(11, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        beginDate = new JFormattedTextField();
        panel1.add(beginDate, cc.xy(9, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label3 = new JLabel();
        label3.setText("���� � ����� ���������");
        panel1.add(label3, cc.xy(7, 7));
        endDate = new JFormattedTextField();
        panel1.add(endDate, cc.xy(9, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
        endTime = new JFormattedTextField();
        panel1.add(endTime, cc.xy(11, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label4 = new JLabel();
        label4.setText("�������� ��������");
        panel1.add(label4, cc.xy(7, 9));
        contestDescription = new JTextArea();
        contestDescription.setLineWrap(true);
        contestDescription.setText("");
        contestDescription.setWrapStyleWord(true);
        panel1.add(contestDescription, cc.xyw(9, 9, 3, CellConstraints.FILL, CellConstraints.FILL));
        final JLabel label5 = new JLabel();
        label5.setText("��� �����������");
        panel1.add(label5, cc.xy(7, 11));
        ownRegistrationRB = new JRadioButton();
        ownRegistrationRB.setText("��������������");
        panel1.add(ownRegistrationRB, cc.xy(9, 11));
        administratorRegistrationRB = new JRadioButton();
        administratorRegistrationRB.setSelected(true);
        administratorRegistrationRB.setText("���������������");
        panel1.add(administratorRegistrationRB, cc.xy(11, 11));
        final JLabel label6 = new JLabel();
        label6.setText("��������� ��������");
        panel1.add(label6, cc.xy(3, 3));
        final JSeparator separator1 = new JSeparator();
        separator1.setOrientation(1);
        panel1.add(separator1, cc.xywh(5, 3, 1, 20, CellConstraints.FILL, CellConstraints.FILL));
        problemName = new JTextField();
        problemName.setText("��� ������");
        panel1.add(problemName, cc.xyw(9, 13, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        serverPlugin = new JTextField();
        panel1.add(serverPlugin, cc.xyw(9, 17, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label7 = new JLabel();
        label7.setText("��� ������");
        panel1.add(label7, cc.xy(7, 13));
        final JLabel label8 = new JLabel();
        label8.setText("���������� ������");
        panel1.add(label8, cc.xy(7, 15));
        final JLabel label9 = new JLabel();
        label9.setText("��������� ������");
        panel1.add(label9, cc.xy(7, 17));
        final JLabel label10 = new JLabel();
        label10.setText("�������");
        panel1.add(label10, cc.xy(7, 19));
        final JLabel label11 = new JLabel();
        label11.setText("�����");
        panel1.add(label11, cc.xy(7, 21));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FormLayout("fill:130dlu:noGrow,left:4dlu:noGrow,fill:32dlu:noGrow", "center:12dlu:noGrow"));
        panel1.add(panel2, cc.xyw(9, 19, 3, CellConstraints.DEFAULT, CellConstraints.FILL));
        problemStatement = new JTextField();
        problemStatement.setText("�������");
        panel2.add(problemStatement, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        changeStatementButton = new JButton();
        changeStatementButton.setText("...");
        panel2.add(changeStatementButton, cc.xy(3, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        clientPlugin = new JTextField();
        clientPlugin.setText("���������� ������");
        panel1.add(clientPlugin, cc.xyw(9, 15, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new FormLayout("fill:130dlu:noGrow,left:4dlu:noGrow,fill:32dlu:noGrow", "center:12dlu:noGrow"));
        panel1.add(panel3, cc.xyw(9, 21, 3, CellConstraints.DEFAULT, CellConstraints.FILL));
        problemAnswer = new JTextField();
        panel3.add(problemAnswer, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        changeAnswerButton = new JButton();
        changeAnswerButton.setText("...");
        panel3.add(changeAnswerButton, cc.xy(3, 1));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(ownRegistrationRB);
        buttonGroup.add(administratorRegistrationRB);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
