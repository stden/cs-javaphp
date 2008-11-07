package ru.ipo.dces.plugins;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import ru.ipo.dces.client.Controller;
import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.pluginapi.PluginEnvironment;
import ru.ipo.dces.clientservercommunication.ContestDescription;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class ManageServerPlugin extends Plugin {
    private JTextField contestName;
    private JFormattedTextField beginDate;
    private JFormattedTextField beginTime;
    private JFormattedTextField endDate;
    private JFormattedTextField endTime;
    private JTextArea contestDescriptionField;
    private JRadioButton administratorRegistrationRB;
    private JRadioButton ownRegistrationRB;
    private JList contestantsList;
    private JTextField fieldTypeName;
    private JButton addButton;
    private JButton deleteButton;
    private JPanel drawPanel;
    private JCheckBox compulsoryFieldCheckBox;
    private JButton OK;

    private static final long serialVersionUID = -4584214565491150823L;

    //TODO Create stretching form instead of fixed one

    /**
     * Инициализация plugin'а
     */
    public ManageServerPlugin(PluginEnvironment env) {
        super(env);

        $$$setupUI$$$();

        env.setTitle("Создать контест");

        setVerifier(beginDate, "dd.MM.yy");
        setVerifier(endDate, "dd.MM.yy");
        setVerifier(beginTime, "H:mm");
        setVerifier(endTime, "H:mm");

        OK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ContestDescription cd = new ContestDescription();

                //TODO fill data in cd
                //TODO create "change field" button

                Controller.addContest(cd);
            }
        });
    }


    public void setData(ManageServerPluginBean data) {
        contestName.setText(data.getContestNameField());
        beginDate.setText(data.getBeginDateField());
        beginDate.setValue(data.getBeginDateField());

        beginTime.setText(data.getBeginTimeField());
        endDate.setText(data.getEndDateField());
        endTime.setText(data.getEndTimeField());
        contestDescriptionField.setText(data.getContestDescriptionField());
        fieldTypeName.setText(data.getContestantNameField());
    }

    public void getData(ManageServerPluginBean data) {
        data.setContestNameField(contestName.getText());
        data.setBeginDateField(beginDate.getText());
        data.setBeginTimeField(beginTime.getText());
        data.setEndDateField(endDate.getText());
        data.setEndTimeField(endTime.getText());
        data.setContestDescriptionField(contestDescriptionField.getText());
        data.setContestantNameField(fieldTypeName.getText());
    }

    public boolean isModified(ManageServerPluginBean data) {
        if (contestName.getText() != null ? !contestName.getText().equals(data.getContestNameField()) : data.getContestNameField() != null)
            return true;
        if (beginDate.getText() != null ? !beginDate.getText().equals(data.getBeginDateField()) : data.getBeginDateField() != null)
            return true;
        if (beginTime.getText() != null ? !beginTime.getText().equals(data.getBeginTimeField()) : data.getBeginTimeField() != null)
            return true;
        if (endDate.getText() != null ? !endDate.getText().equals(data.getEndDateField()) : data.getEndDateField() != null)
            return true;
        if (endTime.getText() != null ? !endTime.getText().equals(data.getEndTimeField()) : data.getEndTimeField() != null)
            return true;
        if (contestDescriptionField.getText() != null ? !contestDescriptionField.getText().equals(data.getContestDescriptionField()) : data.getContestDescriptionField() != null)
            return true;
        if (fieldTypeName.getText() != null ? !fieldTypeName.getText().equals(data.getContestantNameField()) : data.getContestantNameField() != null)
            return true;
        return false;
    }

    private void createUIComponents() {
        drawPanel = this;
    }

    private void setVerifier(JTextField field, String pattern) {

        final JTextField f = field;
        final String pat = pattern;

        if (!pattern.equals("dd.MM.yy") && !pattern.equals("H:mm"))
            throw new IllegalArgumentException();

        field.setInputVerifier(new InputVerifier() {

            public boolean verify(JComponent input) {

                try {
                    new SimpleDateFormat(pat).parse(((JTextField) (input)).getText());
                } catch (ParseException e) {

                    f.setForeground(Color.red);
                    f.setFont(f.getFont().deriveFont(Font.BOLD));
                    f.setText(/*"Введите дату в формате" + */ (pat.equals("dd.MM.yy") ? "дд.мм.гг" : "чч:мм"));

                    f.setForeground(Color.black);

                    return false;
                }

                return true;
            }
        });

        field.setForeground(Color.black);

    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        drawPanel.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:90dlu:noGrow,left:4dlu:noGrow,fill:80dlu:noGrow,left:4dlu:noGrow,fill:81dlu:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:16dlu:noGrow,top:5dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:95px:grow,top:0dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:17dlu:noGrow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:19dlu:noGrow,top:0dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:18dlu:noGrow,top:4dlu:noGrow,center:d:grow"));
        contestName = new JTextField();
        CellConstraints cc = new CellConstraints();
        drawPanel.add(contestName, cc.xyw(5, 1, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label1 = new JLabel();
        label1.setText("Название контеста");
        drawPanel.add(label1, cc.xy(3, 1));
        final JLabel label2 = new JLabel();
        label2.setText("Дата и время начала");
        drawPanel.add(label2, cc.xy(3, 5));
        beginTime = new JFormattedTextField();
        drawPanel.add(beginTime, cc.xy(7, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label3 = new JLabel();
        label3.setText("Дата и время окончания");
        drawPanel.add(label3, cc.xy(3, 9));
        endDate = new JFormattedTextField();
        drawPanel.add(endDate, cc.xy(5, 9, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label4 = new JLabel();
        label4.setText("Описание контеста");
        drawPanel.add(label4, cc.xy(3, 13));
        contestDescriptionField = new JTextArea();
        contestDescriptionField.setLineWrap(true);
        contestDescriptionField.setText("");
        contestDescriptionField.setWrapStyleWord(true);
        drawPanel.add(contestDescriptionField, cc.xyw(5, 13, 3, CellConstraints.FILL, CellConstraints.FILL));
        final JLabel label5 = new JLabel();
        label5.setText("Тип регистрации");
        drawPanel.add(label5, cc.xy(3, 19));
        endTime = new JFormattedTextField();
        drawPanel.add(endTime, cc.xy(7, 9, CellConstraints.FILL, CellConstraints.DEFAULT));
        ownRegistrationRB = new JRadioButton();
        ownRegistrationRB.setText("Самостоятельно");
        drawPanel.add(ownRegistrationRB, cc.xy(5, 19));
        administratorRegistrationRB = new JRadioButton();
        administratorRegistrationRB.setSelected(true);
        administratorRegistrationRB.setText("Администратором");
        drawPanel.add(administratorRegistrationRB, cc.xy(7, 19));
        final JLabel label6 = new JLabel();
        label6.setText("Имя поля");
        label6.setVerticalAlignment(0);
        label6.putClientProperty("html.disable", Boolean.FALSE);
        drawPanel.add(label6, cc.xy(3, 23, CellConstraints.DEFAULT, CellConstraints.FILL));
        fieldTypeName = new JTextField();
        drawPanel.add(fieldTypeName, cc.xy(5, 23, CellConstraints.FILL, CellConstraints.DEFAULT));
        compulsoryFieldCheckBox = new JCheckBox();
        compulsoryFieldCheckBox.setText("обязательное");
        drawPanel.add(compulsoryFieldCheckBox, cc.xy(7, 23));
        addButton = new JButton();
        addButton.setText("Добавить");
        drawPanel.add(addButton, cc.xy(7, 25));
        deleteButton = new JButton();
        deleteButton.setText("Удалить");
        drawPanel.add(deleteButton, cc.xy(7, 27));
        final JSeparator separator1 = new JSeparator();
        drawPanel.add(separator1, cc.xyw(3, 3, 5, CellConstraints.FILL, CellConstraints.FILL));
        final JSeparator separator2 = new JSeparator();
        drawPanel.add(separator2, cc.xywh(3, 7, 5, 2, CellConstraints.FILL, CellConstraints.FILL));
        final JSeparator separator3 = new JSeparator();
        drawPanel.add(separator3, cc.xyw(3, 11, 5, CellConstraints.FILL, CellConstraints.FILL));
        contestantsList = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        defaultListModel1.addElement("Имя");
        defaultListModel1.addElement("Фамилия");
        defaultListModel1.addElement("Школа");
        defaultListModel1.addElement("Класс");
        contestantsList.setModel(defaultListModel1);
        contestantsList.setSelectionMode(0);
        drawPanel.add(contestantsList, cc.xywh(5, 25, 1, 7, CellConstraints.DEFAULT, CellConstraints.FILL));
        OK = new JButton();
        OK.setText("Создать контест");
        drawPanel.add(OK, cc.xyw(3, 37, 5));
        final JSeparator separator4 = new JSeparator();
        drawPanel.add(separator4, cc.xyw(3, 35, 5, CellConstraints.FILL, CellConstraints.FILL));
        final JSeparator separator5 = new JSeparator();
        drawPanel.add(separator5, cc.xyw(3, 17, 5, CellConstraints.FILL, CellConstraints.FILL));
        final JSeparator separator6 = new JSeparator();
        drawPanel.add(separator6, cc.xyw(3, 21, 5, CellConstraints.FILL, CellConstraints.FILL));
        beginDate = new JFormattedTextField();
        drawPanel.add(beginDate, cc.xy(5, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(ownRegistrationRB);
        buttonGroup.add(administratorRegistrationRB);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return drawPanel;
    }
}
