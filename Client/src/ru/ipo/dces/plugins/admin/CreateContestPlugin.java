package ru.ipo.dces.plugins.admin;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import ru.ipo.dces.client.Controller;
import ru.ipo.dces.clientservercommunication.ContestDescription;
import ru.ipo.dces.pluginapi.PluginEnvironment;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.regex.Pattern;

public class CreateContestPlugin extends NotificationPlugin {
    private JTextField contestName;
    private JFormattedTextField beginDate;
    private JFormattedTextField beginTime;
    private JFormattedTextField endDate;
    private JFormattedTextField endTime;
    private JTextArea contestDescription;
    private JRadioButton administratorRegistrationRB;
    private JRadioButton ownRegistrationRB;
    private JList typeNameList;
    private JTextField typeNameField;
    private JButton addButton;
    private JButton deleteButton;
    private JPanel drawPanel;
    private JCheckBox compulsoryFieldCheckBox;
    private JButton createContest;
    private JButton changeButton;
    private JLabel infoMessageLabel;

    private static final long serialVersionUID = -4584214565491150823L;

    //TODO Verify for startDateTime > endDateTime
    //TODO Rewrite plugin with new notification system
    private static Pattern dateValidator = Pattern.compile("(0?[1-9]|[1-2][0-9]|3[01])[\\./](0?[1-9]|1[012])[\\./](19[0-9]{2}|20[0-9]{2}|[0-9]{2})");
    private static Pattern timeValidator = Pattern.compile("([01][0-9]|2[0-3]):[0-5][0-9]");

    private static boolean hasErrors = false;

    private DefaultListModel typeNameModel = new DefaultListModel();

    private final DocumentListener validatorListener = new DocumentListener() {
        public void changedUpdate(DocumentEvent e) {
        }

        public void insertUpdate(DocumentEvent e) {
            setEnabledForAll();
        }

        public void removeUpdate(DocumentEvent e) {
            setEnabledForAll();
        }
    };

    public CreateContestPlugin(PluginEnvironment env) {
        super(env);

        $$$setupUI$$$();

        env.setTitle("Создать контест");

        setEnabledForAll();

        typeNameList.setModel(typeNameModel);
        typeNameList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                typeNameModel.insertElementAt(new TypeNameBean(typeNameField.getText(), compulsoryFieldCheckBox.getModel().isSelected()), 0);

                typeNameList.getSelectionModel().setSelectionInterval(0, 0);

                setEnabledForAll();
            }
        });

        changeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectionIndex = typeNameList.getSelectionModel().getMaxSelectionIndex();

                if (selectionIndex == -1)
                    throw new IndexOutOfBoundsException();

                typeNameModel.set(selectionIndex, new TypeNameBean(typeNameField.getText(), compulsoryFieldCheckBox.getModel().isSelected()));

                setEnabledForAll();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectionIndex = typeNameList.getSelectionModel().getMaxSelectionIndex();

                if (selectionIndex == -1)
                    throw new IndexOutOfBoundsException();

                typeNameModel.remove(selectionIndex);

                typeNameList.getSelectionModel().setSelectionInterval(0, 0);

                setEnabledForAll();
            }
        });

        typeNameList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {

                if (!e.getValueIsAdjusting()) return;

                TypeNameBean value = (TypeNameBean) typeNameList.getSelectedValue();
                if (value == null) return;

                typeNameField.setText(value.toString());
                compulsoryFieldCheckBox.getModel().setSelected(value.isCompulsory());

                setEnabledForAll();
            }
        });

        //set verifiers
        setVerifier(beginDate, FieldType.Date);
        setVerifier(endDate, FieldType.Date);
        setVerifier(beginTime, FieldType.Time);
        setVerifier(endTime, FieldType.Time);

        //set create contest confirmation button listener
        createContest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                ContestDescription cd = new ContestDescription();

                cd.description = contestDescription.getText();
                cd.name = contestName.getText();
                cd.registrationType = ownRegistrationRB.isSelected() ? ContestDescription.RegistrationType.Self : ContestDescription.RegistrationType.ByAdmins;

                try {
                    cd.start = new SimpleDateFormat().parse(beginDate.getText() + " " + beginTime.getText());
                    cd.finish = new SimpleDateFormat().parse(endDate.getText() + " " + endTime.getText());
                } catch (ParseException e1) {
                    throw new IllegalArgumentException();
                }

                cd.data = new String[typeNameModel.getSize()];
                cd.compulsory = new boolean[typeNameModel.getSize()];

                for (int i = 0; i < typeNameModel.getSize(); i++) {
                    cd.data[i] = ((TypeNameBean) typeNameModel.get(i)).getName();
                    cd.compulsory[i] = ((TypeNameBean) typeNameModel.get(i)).isCompulsory();
                }

                boolean setstatus = Controller.addContest(cd);

                if (setstatus) {
                    JOptionPane.showMessageDialog(null, "Контест успешно добавлен", "Создание контеста", JOptionPane.INFORMATION_MESSAGE);

                    contestName.setText("");
                    contestDescription.setText("");
                    beginDate.setText("");
                    beginTime.setText("");
                    endDate.setText("");
                    endTime.setText("");
                    administratorRegistrationRB.setSelected(true);
                    typeNameModel.clear();
                    typeNameField.setText("");
                }
                else
                    JOptionPane.showMessageDialog(null, "Произошла ошибка при создании контеста", "Создание контеста", JOptionPane.ERROR_MESSAGE);
            }
        });

        addValidatorListeners(contestName.getDocument());
        addValidatorListeners(beginDate.getDocument());
        addValidatorListeners(beginTime.getDocument());
        addValidatorListeners(endDate.getDocument());
        addValidatorListeners(endTime.getDocument());
        addValidatorListeners(contestDescription.getDocument());

        typeNameField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                setEnabledForAll();
            }

            public void removeUpdate(DocumentEvent e) {
                setEnabledForAll();
            }

            public void changedUpdate(DocumentEvent e) {
            }
        });

        compulsoryFieldCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setEnabledForAll();
            }
        });
    }

    private void addValidatorListeners(final Document d) {
        d.addDocumentListener(validatorListener);
    }

    private void setVerifier(final JTextField field, final FieldType type) {

        field.setInputVerifier(new InputVerifier() {

            public boolean verify(JComponent input) {

                String inputText = ((JTextField) (input)).getText();
                //if input is empty and we previously had errors then clear error field rendering and do not change any other status
                //if input is correct and we previously didn't have any errors then do nothing


                if (inputText.equals("") && hasErrors) {
                    renderField(field, NotificationType.Info);
                } else if (isInputCorrect(inputText, type) && hasErrors) {

                } else if (isInputCorrect(inputText, type)) {

                    renderField(field, NotificationType.Error);
                    fireNotificationMessage(infoMessageLabel, "Введите корректную дату (дд.мм.гг) и время (чч:мм)", NotificationType.Error);
                    hasErrors = true;

                } else {
                    fireNotificationMessage(infoMessageLabel, "", NotificationType.Info);
                    renderField(field, NotificationType.Info);
                    hasErrors = false;
                }

                setEnabledForAll();

                return true;
            }
        });
    }

    private boolean isInputCorrect(String inputText, FieldType type) {
        return type.equals(FieldType.Date) && !dateValidator.matcher(inputText).matches() ||
                type.equals(FieldType.Time) && !timeValidator.matcher(inputText).matches();
    }

    private void renderField(JTextField field, NotificationType type) {
        switch (type) {
            case Error:
                field.setBackground(new Color(255, 220, 220));
            case Confirm:
                field.setBackground(Color.WHITE);
            case Info:
                field.setBackground(Color.WHITE);
                break;
            case Warning:
                field.setBackground(new Color(220, 255, 255));
                break;
        }
    }

    private void setEnabledForAll() {
        createContest.setEnabled(validateForCreateContestButton());

        addButton.setEnabled(validateForAddButton());
        changeButton.setEnabled(validateForChangeButton());
        deleteButton.setEnabled(validateForDeleteButton());
    }

    boolean validateForCreateContestButton() {
        return typeNameModel.getSize() > 0 &&
                contestName.getDocument().getLength() > 0 &&
                beginDate.getDocument().getLength() > 0 &&
                beginTime.getDocument().getLength() > 0 &&
                endTime.getDocument().getLength() > 0 &&
                endDate.getDocument().getLength() > 0 &&
                contestDescription.getDocument().getLength() > 0 &&
                !hasErrors;
    }

    private boolean validateForDeleteButton() {
        int selectionIndex = typeNameList.getSelectionModel().getMinSelectionIndex();
        boolean isSelected = selectionIndex != -1;

        //1
        if (!isSelected)
            return false;

        if (selectionIndex >= typeNameModel.getSize()) return false;
        TypeNameBean selected = (TypeNameBean) typeNameModel.get(selectionIndex);
        if (selected == null) return false;

        //2
        return selected.getName().equals(typeNameField.getText()) && selected.isCompulsory() == compulsoryFieldCheckBox.getModel().isSelected();

    }

    private boolean validateForChangeButton() {
        int selectionIndex = typeNameList.getSelectionModel().getMinSelectionIndex();

        boolean isSelected = selectionIndex != -1;

        //1
        if (!isSelected)
            return false;

        String text = typeNameField.getText();

        //2
        if (text.equals(""))
            return false;

        if (selectionIndex >= typeNameModel.getSize()) return false;
        TypeNameBean selected = (TypeNameBean) typeNameModel.get(selectionIndex);
        if (selected == null) return false;
        boolean isDifferent = !(selected.getName().equals(text) && selected.isCompulsory() == compulsoryFieldCheckBox.getModel().isSelected());

        //3
        if (!isDifferent)
            return false;

        //4

        boolean noRepeat = true;
        for (int i = 0; i < typeNameModel.getSize(); i++)
            if (i != selectionIndex)
                noRepeat = noRepeat && !((TypeNameBean) typeNameModel.get(i)).getName().equals(text);

        return noRepeat;
    }

    private boolean validateForAddButton() {
        String fieldText = typeNameField.getText();

        boolean res = !fieldText.equals("");

        boolean noRepeat = true;

        for (int i = 0; i < typeNameModel.getSize(); i++)
            noRepeat = noRepeat && !((TypeNameBean) typeNameModel.getElementAt(i)).getName().equals(fieldText);

        return res && noRepeat;
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
        drawPanel.setLayout(new FormLayout("fill:max(d;4px):noGrow,fill:4dlu:noGrow,fill:92dlu:noGrow,left:4dlu:noGrow,fill:80dlu:grow,left:4dlu:noGrow,fill:80dlu:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:max(d;4px):noGrow,top:12dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:60dlu:grow,top:0dlu:noGrow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:23dlu:noGrow,top:4dlu:noGrow,top:4dlu:noGrow,center:d:grow,center:16dlu:noGrow,center:max(d;4px):noGrow"));
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
        typeNameField = new JTextField();
        drawPanel.add(typeNameField, cc.xy(5, 26, CellConstraints.FILL, CellConstraints.DEFAULT));
        compulsoryFieldCheckBox = new JCheckBox();
        compulsoryFieldCheckBox.setText("обязательное");
        drawPanel.add(compulsoryFieldCheckBox, cc.xy(7, 26));
        final JSeparator separator1 = new JSeparator();
        drawPanel.add(separator1, cc.xyw(3, 7, 5, CellConstraints.FILL, CellConstraints.FILL));
        final JSeparator separator2 = new JSeparator();
        drawPanel.add(separator2, cc.xywh(3, 11, 5, 2, CellConstraints.FILL, CellConstraints.FILL));
        final JSeparator separator3 = new JSeparator();
        drawPanel.add(separator3, cc.xyw(3, 15, 5, CellConstraints.FILL, CellConstraints.FILL));
        typeNameList = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        typeNameList.setModel(defaultListModel1);
        typeNameList.setSelectionMode(0);
        drawPanel.add(typeNameList, cc.xywh(5, 28, 1, 7, CellConstraints.DEFAULT, CellConstraints.FILL));
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

    private void createUIComponents() {
        drawPanel = this;
    }

    private class TypeNameBean {
        private String name;
        private boolean isCompulsory;

        private TypeNameBean(String name, boolean compulsory) {
            this.name = name;
            isCompulsory = compulsory;
        }

        public String getName() {
            return name;
        }

        public boolean isCompulsory() {
            return isCompulsory;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCompulsory(boolean compulsory) {
            isCompulsory = compulsory;
        }

        public String toString() {
            return name + (isCompulsory ? " (*)" : "");
        }
    }
}