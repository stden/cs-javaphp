package ru.ipo.dces.plugins.admin;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import ru.ipo.dces.client.Controller;
import ru.ipo.dces.clientservercommunication.ContestDescription;
import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.pluginapi.PluginEnvironment;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

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
    private JLabel infoMessageLabel;

    private static final long serialVersionUID = -4584214565491150823L;
    private static Pattern dateValidator = Pattern.compile("(0?[1-9]|[1-2][0-9]|3[01])[\\./](0?[1-9]|1[012])[\\./](19[0-9]{2}|20[0-9]{2}|[0-9]{2})");
    private static Pattern timeValidator = Pattern.compile("([01][0-9]|2[0-3]):[0-5][0-9]");

    private int checkSum = -2; //TODO -1 to discount one edit which is not compulsory, and -1 to JList field
    private boolean hasErrors = false;

    private final DocumentListener validatorListener = new DocumentListener() {
        public void changedUpdate(DocumentEvent e) {
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
    };

    private final DocumentListener fieldTypeListener = new DocumentListener() {
        public void insertUpdate(DocumentEvent e) {

           /* for(typeFieldList.ge)
            if(.e.getDocument().getText(0, e.getDocument().getLength()))*/
        }

        public void removeUpdate(DocumentEvent e) {
            if (e.getDocument().getLength() == 0) {
                addButton.setEnabled(false);
                deleteButton.setEnabled(false);
                changeButton.setEnabled(false);
            }
        }

        public void changedUpdate(DocumentEvent e) {
        }
    };

    public CreateContestPlugin(PluginEnvironment env) {
        super(env);

        $$$setupUI$$$();

        env.setTitle("Создать контест");

        //set verifiers
        setVerifier(beginDate, FieldType.Date);
        setVerifier(endDate, FieldType.Date);
        setVerifier(beginTime, FieldType.Time);
        setVerifier(endTime, FieldType.Time);

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

        addValidatorListeners(contestName.getDocument());
        addValidatorListeners(beginDate.getDocument());
        addValidatorListeners(beginTime.getDocument());
        addValidatorListeners(endDate.getDocument());
        addValidatorListeners(endTime.getDocument());
        addValidatorListeners(contestDescription.getDocument());

        typeFieldList.getModel().addListDataListener(new ListDataListener() {
            public void intervalAdded(ListDataEvent e) {
                checkSum++;
            }

            public void intervalRemoved(ListDataEvent e) {
                checkSum--;
            }

            public void contentsChanged(ListDataEvent e) {
            }
        });

        typeFieldList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                fieldTypeName.setText(((String) typeFieldList.getSelectedValue()));
                //ListModel lm = typeFieldList.getModel();
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

    private void addValidatorListeners(final Document d) {
        d.addDocumentListener(validatorListener);
    }

    private void createUIComponents() {
        drawPanel = this;
    }

    private void setVerifier(final JTextField field, final FieldType type) {

        field.setInputVerifier(new InputVerifier() {

            //TODO fix a bug with coplex interaction with fields
            public boolean verify(JComponent input) {

                String inputText = ((JTextField) (input)).getText();

                if (inputText.equals("") && hasErrors) {
                    //fireNotificationMessage("", NotificationType.Info);
                    field.setBackground(Color.WHITE);
                } else if (type.equals(FieldType.Date) && !dateValidator.matcher(inputText).matches() ||
                        type.equals(FieldType.Time) && !timeValidator.matcher(inputText).matches()) {

                    field.setBackground(new Color(255, 240, 240));
                    fireNotificationMessage("Введите корректную дату (дд.мм.гг) и время (чч:мм)", NotificationType.Error);
                    hasErrors = true;

                } else {
                    fireNotificationMessage("", NotificationType.Info);
                    field.setBackground(Color.WHITE);
                    hasErrors = false;
                }
                return true;
            }
        });
    }

    private void fireNotificationMessage(String s, NotificationType type) {

        infoMessageLabel.setText(s);

        switch (type) {
            case Error:
                infoMessageLabel.setForeground(new Color(255, 100, 100));
                break;
            case Info:
                infoMessageLabel.setForeground(Color.BLACK);
                break;
            case Warning:
                infoMessageLabel.setForeground(new Color(100, 255, 255));
                break;
        }

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
        drawPanel.setLayout(new FormLayout("fill:max(d;4px):noGrow,fill:4dlu:noGrow,fill:92dlu:noGrow,left:4dlu:noGrow,fill:80dlu:noGrow,left:4dlu:noGrow,fill:80dlu:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:max(d;4px):noGrow,top:12dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:60dlu:grow,top:0dlu:noGrow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:23dlu:noGrow,top:4dlu:noGrow,top:4dlu:noGrow,center:d:grow,center:16dlu:noGrow,center:max(d;4px):noGrow"));
        contestName = new JTextField();
        CellConstraints cc = new CellConstraints();
        drawPanel.add(contestName, cc.xyw(5, 5, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label1 = new JLabel();
        label1.setText("Название контеста");
        drawPanel.add(label1, cc.xy(3, 5));
        final JLabel label2 = new JLabel();
        label2.setText("Дата и время начала");
        drawPanel.add(label2, cc.xy(3, 9));
        beginTime = new JFormattedTextField();
        drawPanel.add(beginTime, cc.xy(7, 9, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label3 = new JLabel();
        label3.setText("Дата и время окончания");
        drawPanel.add(label3, cc.xy(3, 13));
        endDate = new JFormattedTextField();
        drawPanel.add(endDate, cc.xy(5, 13, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label4 = new JLabel();
        label4.setText("Описание контеста");
        drawPanel.add(label4, cc.xy(3, 17));
        contestDescription = new JTextArea();
        contestDescription.setLineWrap(true);
        contestDescription.setText("");
        contestDescription.setWrapStyleWord(true);
        drawPanel.add(contestDescription, cc.xyw(5, 17, 3, CellConstraints.FILL, CellConstraints.FILL));
        final JLabel label5 = new JLabel();
        label5.setText("Тип регистрации");
        drawPanel.add(label5, cc.xy(3, 22));
        endTime = new JFormattedTextField();
        drawPanel.add(endTime, cc.xy(7, 13, CellConstraints.FILL, CellConstraints.DEFAULT));
        ownRegistrationRB = new JRadioButton();
        ownRegistrationRB.setText("Самостоятельно");
        drawPanel.add(ownRegistrationRB, cc.xy(5, 22));
        administratorRegistrationRB = new JRadioButton();
        administratorRegistrationRB.setSelected(true);
        administratorRegistrationRB.setText("Администратором");
        drawPanel.add(administratorRegistrationRB, cc.xy(7, 22));
        final JLabel label6 = new JLabel();
        label6.setText("Имя поля");
        label6.setVerticalAlignment(0);
        label6.putClientProperty("html.disable", Boolean.FALSE);
        drawPanel.add(label6, cc.xy(3, 26, CellConstraints.DEFAULT, CellConstraints.FILL));
        fieldTypeName = new JTextField();
        drawPanel.add(fieldTypeName, cc.xy(5, 26, CellConstraints.FILL, CellConstraints.DEFAULT));
        compulsoryFieldCheckBox = new JCheckBox();
        compulsoryFieldCheckBox.setText("обязательное");
        drawPanel.add(compulsoryFieldCheckBox, cc.xy(7, 26));
        final JSeparator separator1 = new JSeparator();
        drawPanel.add(separator1, cc.xyw(3, 7, 5, CellConstraints.FILL, CellConstraints.FILL));
        final JSeparator separator2 = new JSeparator();
        drawPanel.add(separator2, cc.xywh(3, 11, 5, 2, CellConstraints.FILL, CellConstraints.FILL));
        final JSeparator separator3 = new JSeparator();
        drawPanel.add(separator3, cc.xyw(3, 15, 5, CellConstraints.FILL, CellConstraints.FILL));
        typeFieldList = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        defaultListModel1.addElement("Имя");
        defaultListModel1.addElement("Фамилия");
        defaultListModel1.addElement("Школа");
        defaultListModel1.addElement("Класс");
        typeFieldList.setModel(defaultListModel1);
        typeFieldList.setSelectionMode(0);
        drawPanel.add(typeFieldList, cc.xywh(5, 28, 1, 7, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JSeparator separator4 = new JSeparator();
        drawPanel.add(separator4, cc.xyw(3, 20, 5, CellConstraints.FILL, CellConstraints.FILL));
        final JSeparator separator5 = new JSeparator();
        drawPanel.add(separator5, cc.xyw(3, 24, 5, CellConstraints.FILL, CellConstraints.FILL));
        beginDate = new JFormattedTextField();
        drawPanel.add(beginDate, cc.xy(5, 9, CellConstraints.FILL, CellConstraints.DEFAULT));
        addButton = new JButton();
        addButton.setEnabled(false);
        addButton.setText("Добавить");
        drawPanel.add(addButton, cc.xy(7, 30));
        deleteButton = new JButton();
        deleteButton.setEnabled(false);
        deleteButton.setText("Удалить");
        drawPanel.add(deleteButton, cc.xy(7, 32));
        changeButton = new JButton();
        changeButton.setEnabled(false);
        changeButton.setText("Изменить");
        drawPanel.add(changeButton, cc.xy(7, 28));
        createContest = new JButton();
        createContest.setEnabled(false);
        createContest.setText("Создать");
        drawPanel.add(createContest, cc.xyw(3, 38, 5, CellConstraints.FILL, CellConstraints.FILL));
        final JSeparator separator6 = new JSeparator();
        drawPanel.add(separator6, cc.xywh(3, 36, 5, 2, CellConstraints.FILL, CellConstraints.FILL));
        infoMessageLabel = new JLabel();
        infoMessageLabel.setBackground(new Color(-986896));
        infoMessageLabel.setText("Пожалуйста, введите информацию о контесте");
        drawPanel.add(infoMessageLabel, cc.xyw(3, 2, 5, CellConstraints.DEFAULT, CellConstraints.FILL));
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

    private enum FieldType {
        Date, Time
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

}
