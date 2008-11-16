package ru.ipo.dces.plugins.admin;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import ru.ipo.dces.client.Controller;
import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.pluginapi.PluginEnvironment;
import ru.ipo.dces.clientservercommunication.ContestDescription;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;

public class CreateContestPlugin extends Plugin {
    private JTextField contestName;
    private JFormattedTextField beginDate;
    private JFormattedTextField beginTime;
    private JFormattedTextField endDate;
    private JFormattedTextField endTime;
    private JTextArea contestDescription;
    private JRadioButton administratorRegistrationRB;
    private JRadioButton ownRegistrationRB;
    private JList typeFieldList;
    private JTextField fieldTypeName;
    private JButton addButton;
    private JButton deleteButton;
    private JPanel drawPanel;
    private JCheckBox compulsoryFieldCheckBox;
    private JButton createContest;
    private JButton changeButton;

    private static final long serialVersionUID = -4584214565491150823L;

    private int checkSum = -2; //TODO -1 to discount one edit which is not compulsory, and -1 to JList field

    public int getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(int checkSum) {
        this.checkSum = checkSum;
    }

    /**
     * Инициализация plugin'а
     */
    public CreateContestPlugin(PluginEnvironment env) {
        super(env);

        $$$setupUI$$$();

        env.setTitle("Создать контест");

        //set verifiers
        setVerifier(beginDate, "dd.MM.yy");
        setVerifier(endDate, "dd.MM.yy");
        setVerifier(beginTime, "H:mm");
        setVerifier(endTime, "H:mm");

        //set create contest confirmation button listener
        createContest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                ContestDescription cd = new ContestDescription();

                //TODO fill data in cd (???)
                Controller.addContest(cd);
            }
        });

        //calculate a number of validated components
        for (Component c : this.getComponents())
            if (c instanceof JTextField || c instanceof JTextArea || c instanceof JList)
                checkSum++;

        contestName.addPropertyChangeListener("text", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                System.out.println("evt = " + evt);
            }
        }
        );

        addDocumentListeners(contestName.getDocument());
        addDocumentListeners(beginDate.getDocument());
        addDocumentListeners(beginTime.getDocument());
        addDocumentListeners(endDate.getDocument());
        addDocumentListeners(endTime.getDocument());
        addDocumentListeners(contestDescription.getDocument());

        typeFieldList.addPropertyChangeListener("model", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (((ListModel) evt.getNewValue()).getSize() == 0 && ((ListModel) evt.getOldValue()).getSize() != 0)
                    checkSum--;
                else if (((ListModel) evt.getNewValue()).getSize() != 0 && ((ListModel) evt.getOldValue()).getSize() == 0)
                    checkSum++;

                checkCreateContestButton();
            }
        });


        typeFieldList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                fieldTypeName.setText(((String) typeFieldList.getSelectedValue()));
                ListModel lm = typeFieldList.getModel();
            }
        });


        fieldTypeName.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);    //To change body of overridden methods use File | Settings | File Templates.
            }
        });
    }

    private void checkCreateContestButton() {
        if (checkSum == 0)
            createContest.setEnabled(true);
        else if (checkSum > 0)
            createContest.setEnabled(false);
        else
            throw new NumberFormatException();
    }

    private void addDocumentListeners(final Document d) {
        d.addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {

                int i = e.getLength();

                if (i == 0)
                    checkSum--;
                else if (i == 1)
                    checkSum++;

                checkCreateContestButton();
            }

            public void insertUpdate(DocumentEvent e) {

                if (e.getDocument().getLength() == 1)
                    checkSum--;

                checkCreateContestButton();
            }

            public void removeUpdate(DocumentEvent e) {
                if (e.getDocument().getLength() == 0)
                    checkSum++;

                checkCreateContestButton();
            }
        });
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

                Controller.ClientNotificator.fireNotificationMessage("Поле должно быть в формате: "+ (pat.equals("dd.MM.yy") ? "дд.мм.гг" : "чч:мм"));
                /*try {
                    new SimpleDateFormat(pat).parse(((JTextField) (input)).getText());
                } catch (ParseException e) {

                    f.setForeground(Color.red);
                    f.setFont(f.getFont().deriveFont(Font.BOLD));
                    f.setText(*//*"Введите дату в формате" + *//* (pat.equals("dd.MM.yy") ? "дд.мм.гг" : "чч:мм"));

                    f.setForeground(Color.black);

                    return false;
                }*/

                return true;
            }
        });

        field.setForeground(Color.black);

    }

    /*public void setData(CreateContestPluginBean data) {
        contestName.setText(data.getContestNameField());
        beginTime.setText(data.getBeginTimeField());
        endDate.setText(data.getEndDateField());
        contestDescription.setText(data.getContestDescriptionField());
        endTime.setText(data.getEndTimeField());
        fieldTypeName.setText(data.getContestantNameField());
        compulsoryFieldCheckBox.setSelected(data.isCompulsoryFieldCheckbox());
        beginDate.setText(data.getBeginDateField());
    }

    public void getData(CreateContestPluginBean data) {
        data.setContestNameField(contestName.getText());
        data.setBeginTimeField(beginTime.getText());
        data.setEndDateField(endDate.getText());
        data.setContestDescriptionField(contestDescription.getText());
        data.setEndTimeField(endTime.getText());
        data.setContestantNameField(fieldTypeName.getText());
        data.setCompulsoryFieldCheckbox(compulsoryFieldCheckBox.isSelected());
        data.setBeginDateField(beginDate.getText());
    }

    public boolean isModified(CreateContestPluginBean data) {
        if (contestName.getText() != null ? !contestName.getText().equals(data.getContestNameField()) : data.getContestNameField() != null)
            return true;
        if (beginTime.getText() != null ? !beginTime.getText().equals(data.getBeginTimeField()) : data.getBeginTimeField() != null)
            return true;
        if (endDate.getText() != null ? !endDate.getText().equals(data.getEndDateField()) : data.getEndDateField() != null)
            return true;
        if (contestDescription.getText() != null ? !contestDescription.getText().equals(data.getContestDescriptionField()) : data.getContestDescriptionField() != null)
            return true;
        if (endTime.getText() != null ? !endTime.getText().equals(data.getEndTimeField()) : data.getEndTimeField() != null)
            return true;
        if (fieldTypeName.getText() != null ? !fieldTypeName.getText().equals(data.getContestantNameField()) : data.getContestantNameField() != null)
            return true;
        if (compulsoryFieldCheckBox.isSelected() != data.isCompulsoryFieldCheckbox()) return true;
        if (beginDate.getText() != null ? !beginDate.getText().equals(data.getBeginDateField()) : data.getBeginDateField() != null)
            return true;
        return false;
    }*/

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        drawPanel.setLayout(new FormLayout("fill:max(d;4px):noGrow,fill:4dlu:noGrow,fill:92dlu:noGrow,left:4dlu:noGrow,fill:80dlu:noGrow,left:4dlu:noGrow,fill:81dlu:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:18dlu:noGrow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:95px:grow,top:0dlu:noGrow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:17dlu:noGrow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:23dlu:noGrow,top:4dlu:noGrow,top:4dlu:noGrow,center:d:grow,center:26px:noGrow,center:max(d;4px):noGrow"));
        contestName = new JTextField();
        CellConstraints cc = new CellConstraints();
        drawPanel.add(contestName, cc.xyw(5, 3, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label1 = new JLabel();
        label1.setText("Название контеста");
        drawPanel.add(label1, cc.xy(3, 3));
        final JLabel label2 = new JLabel();
        label2.setText("Дата и время начала");
        drawPanel.add(label2, cc.xy(3, 7));
        beginTime = new JFormattedTextField();
        drawPanel.add(beginTime, cc.xy(7, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label3 = new JLabel();
        label3.setText("Дата и время окончания");
        drawPanel.add(label3, cc.xy(3, 11));
        endDate = new JFormattedTextField();
        drawPanel.add(endDate, cc.xy(5, 11, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label4 = new JLabel();
        label4.setText("Описание контеста");
        drawPanel.add(label4, cc.xy(3, 15));
        contestDescription = new JTextArea();
        contestDescription.setLineWrap(true);
        contestDescription.setText("");
        contestDescription.setWrapStyleWord(true);
        drawPanel.add(contestDescription, cc.xyw(5, 15, 3, CellConstraints.FILL, CellConstraints.FILL));
        final JLabel label5 = new JLabel();
        label5.setText("Тип регистрации");
        drawPanel.add(label5, cc.xy(3, 20));
        endTime = new JFormattedTextField();
        drawPanel.add(endTime, cc.xy(7, 11, CellConstraints.FILL, CellConstraints.DEFAULT));
        ownRegistrationRB = new JRadioButton();
        ownRegistrationRB.setText("Самостоятельно");
        drawPanel.add(ownRegistrationRB, cc.xy(5, 20));
        administratorRegistrationRB = new JRadioButton();
        administratorRegistrationRB.setSelected(true);
        administratorRegistrationRB.setText("Администратором");
        drawPanel.add(administratorRegistrationRB, cc.xy(7, 20));
        final JLabel label6 = new JLabel();
        label6.setText("Имя поля");
        label6.setVerticalAlignment(0);
        label6.putClientProperty("html.disable", Boolean.FALSE);
        drawPanel.add(label6, cc.xy(3, 24, CellConstraints.DEFAULT, CellConstraints.FILL));
        fieldTypeName = new JTextField();
        drawPanel.add(fieldTypeName, cc.xy(5, 24, CellConstraints.FILL, CellConstraints.DEFAULT));
        compulsoryFieldCheckBox = new JCheckBox();
        compulsoryFieldCheckBox.setText("обязательное");
        drawPanel.add(compulsoryFieldCheckBox, cc.xy(7, 24));
        final JSeparator separator1 = new JSeparator();
        drawPanel.add(separator1, cc.xyw(3, 5, 5, CellConstraints.FILL, CellConstraints.FILL));
        final JSeparator separator2 = new JSeparator();
        drawPanel.add(separator2, cc.xywh(3, 9, 5, 2, CellConstraints.FILL, CellConstraints.FILL));
        final JSeparator separator3 = new JSeparator();
        drawPanel.add(separator3, cc.xyw(3, 13, 5, CellConstraints.FILL, CellConstraints.FILL));
        typeFieldList = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        defaultListModel1.addElement("Имя");
        defaultListModel1.addElement("Фамилия");
        defaultListModel1.addElement("Школа");
        defaultListModel1.addElement("Класс");
        typeFieldList.setModel(defaultListModel1);
        typeFieldList.setSelectionMode(0);
        drawPanel.add(typeFieldList, cc.xywh(5, 26, 1, 7, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JSeparator separator4 = new JSeparator();
        drawPanel.add(separator4, cc.xyw(3, 18, 5, CellConstraints.FILL, CellConstraints.FILL));
        final JSeparator separator5 = new JSeparator();
        drawPanel.add(separator5, cc.xyw(3, 22, 5, CellConstraints.FILL, CellConstraints.FILL));
        beginDate = new JFormattedTextField();
        drawPanel.add(beginDate, cc.xy(5, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
        addButton = new JButton();
        addButton.setEnabled(false);
        addButton.setText("Добавить");
        drawPanel.add(addButton, cc.xy(7, 28));
        deleteButton = new JButton();
        deleteButton.setEnabled(false);
        deleteButton.setText("Удалить");
        drawPanel.add(deleteButton, cc.xy(7, 30));
        changeButton = new JButton();
        changeButton.setEnabled(false);
        changeButton.setText("Изменить");
        drawPanel.add(changeButton, cc.xy(7, 26));
        createContest = new JButton();
        createContest.setEnabled(false);
        createContest.setText("Создать");
        drawPanel.add(createContest, cc.xyw(3, 36, 5, CellConstraints.FILL, CellConstraints.FILL));
        final JSeparator separator6 = new JSeparator();
        drawPanel.add(separator6, cc.xywh(3, 34, 5, 2, CellConstraints.FILL, CellConstraints.FILL));
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
