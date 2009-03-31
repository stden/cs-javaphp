package ru.ipo.dces.plugins.admin;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.Sizes;
import ru.ipo.dces.client.Controller;
import ru.ipo.dces.client.RequestResponseUtils;
import ru.ipo.dces.clientservercommunication.ContestDescription;
import ru.ipo.dces.clientservercommunication.UserDescription;
import ru.ipo.dces.clientservercommunication.UserDataField;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;
import ru.ipo.dces.exceptions.ServerReturnedError;
import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.pluginapi.PluginEnvironment;
import ru.ipo.dces.plugins.admin.beans.ContestsListBean;
import ru.ipo.dces.plugins.admin.beans.ManageUsersPluginBean;
import ru.ipo.dces.plugins.admin.beans.UsersListBean;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManageUsersPlugin extends JPanel implements Plugin {
    private JPanel drawPanel;
    private JList contestsList;
    private JLabel contestsLabel;
    private JList usersList;
    private JButton addButton;
    private JButton deleteButton;
    private JButton changeButton;
    private JUserTable userDataTable;
    private JRadioButton superAdminCB;
    private JRadioButton participantCB;
    private JRadioButton contestAdminCB;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private DefaultListModel contestsListModel = new DefaultListModel();
    private DefaultListModel usersListModel = new DefaultListModel();

    private ManageUsersPluginBean oldBean = null;
    private ManageUsersPluginBean newBean = null;

    /**
     * Инициализация plugin'а
     *
     * @param env environment for the plugin
     */
    public ManageUsersPlugin(PluginEnvironment env) {

        $$$setupUI$$$();

        env.setTitle("Пользователи");

        contestsList.setModel(contestsListModel);
        usersList.setModel(usersListModel);

        contestsList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                ContestsListBean cb = (ContestsListBean) contestsList.getSelectedValue();

                oldBean = null;
                newBean = new ManageUsersPluginBean();

                if (cb == null)
                    return;

                fillDaFormWithData(cb.getDescription().contestID, RequestResponseUtils.extractFieldNames(cb.getDescription().data));
            }
        });

        usersList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                UsersListBean ulb = (UsersListBean) usersList.getSelectedValue();

                if (ulb == null)
                    return;

                usernameField.setText(ulb.getDescription().login);
                passwordField.setText("");

                userDataTable.setValues(ulb.getDescription().dataValue);

                switch (ulb.getDescription().userType) {
                    case ContestAdmin:
                        contestAdminCB.setSelected(true);
                        break;
                    case SuperAdmin:
                        superAdminCB.setSelected(true);
                        break;
                    case Participant:
                        participantCB.setSelected(true);
                }

                oldBean = new ManageUsersPluginBean();

                oldBean.setType(ulb.getDescription().userType);
                oldBean.setLogin(ulb.getDescription().login);
                oldBean.setPassword(new char[0]);
                oldBean.setValues(ulb.getDescription().dataValue);

                newBean = new ManageUsersPluginBean(oldBean);
            }
        });

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                ContestsListBean selectedContest = (ContestsListBean) (contestsList.getSelectedValue());

                if (selectedContest == null)
                    return;

                try {
                    Controller.addUser(newBean.getLogin(),
                            newBean.getPassword(),
                            newBean.getValues(),
                            newBean.getType(),
                            selectedContest.getDescription().contestID);

                } catch (ServerReturnedError serverReturnedError) {
                    JOptionPane.showMessageDialog(null, "Сервер вернул ошибку: " + serverReturnedError, "Ошибка сервера", JOptionPane.ERROR_MESSAGE);
                } catch (GeneralRequestFailureException serverReturnedNoAnswer) {
                    JOptionPane.showMessageDialog(null, "Не удалось связаться с сервером", "Ошибка сервера", JOptionPane.ERROR_MESSAGE);
                }

                fillDaFormWithData(selectedContest.getDescription().contestID, RequestResponseUtils.extractFieldNames(selectedContest.getDescription().data));
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                UsersListBean ulb = (UsersListBean) usersList.getSelectedValue();

                if (ulb == null)
                    return;

                try {
                    Controller.removeUser(ulb.getDescription().userID);
                } catch (ServerReturnedError serverReturnedError) {
                    JOptionPane.showMessageDialog(null, "Сервер вернул ошибку: " + serverReturnedError, "Ошибка сервера", JOptionPane.ERROR_MESSAGE);
                } catch (GeneralRequestFailureException serverReturnedNoAnswer) {
                    JOptionPane.showMessageDialog(null, "Не удалось связаться с сервером", "Ошибка сервера", JOptionPane.ERROR_MESSAGE);
                }

                ContestsListBean value = (ContestsListBean) contestsList.getSelectedValue();

                if (value == null)
                    fillDaFormWithData(-1, null);
                else
                    fillDaFormWithData(value.getDescription().contestID, RequestResponseUtils.extractFieldNames(value.getDescription().data));
            }
        });

        ActionListener rbListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (newBean == null)
                    return;

                if (superAdminCB.isSelected())
                    newBean.setType(UserDescription.UserType.SuperAdmin);
                else if (participantCB.isSelected())
                    newBean.setType(UserDescription.UserType.Participant);
                else if (contestAdminCB.isSelected())
                    newBean.setType(UserDescription.UserType.ContestAdmin);
            }
        };

        superAdminCB.addActionListener(rbListener);
        participantCB.addActionListener(rbListener);
        contestAdminCB.addActionListener(rbListener);


        usernameField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateFields();
            }

            public void removeUpdate(DocumentEvent e) {
                updateFields();
            }

            public void changedUpdate(DocumentEvent e) {
            }

            private void updateFields() {
                if (newBean == null)
                    return;

                newBean.setLogin(usernameField.getText());
            }
        });


        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateFields();
            }

            public void removeUpdate(DocumentEvent e) {
                updateFields();
            }

            public void changedUpdate(DocumentEvent e) {
            }

            private void updateFields() {
                if (newBean == null)
                    return;

                newBean.setPassword(passwordField.getPassword());
            }
        });

        userDataTable.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {

                if (e.getColumn() != 2) return;
                if (e.getFirstRow() != e.getLastRow()) return;

                newBean.setValue(e.getFirstRow(), userDataTable.getValue(e.getFirstRow()));
            }
        });
    }

  private void setColumn(FormLayout form, int colNo, int xSize, double grow) {
        form.setColumnSpec(colNo, new ColumnSpec(ColumnSpec.FILL, Sizes.dluX(xSize), grow));
    }

    private void refreshContestsList() {
        contestsListModel.clear();

        ContestDescription zeroContestDescription = new ContestDescription();
        //other fields won't be filled
        zeroContestDescription.name = "Список администраторов";
        zeroContestDescription.contestID = 0;
        zeroContestDescription.data = new UserDataField[]{};

        contestsListModel.addElement(new ContestsListBean(zeroContestDescription));

        ContestDescription[] contestDescriptions = Controller.getAvailableContests();

        for (ContestDescription description : contestDescriptions)
            contestsListModel.addElement(new ContestsListBean(description));
    }


    public JPanel getPanel() {
        return this;
    }

    public void activate() {

        FormLayout cur = (FormLayout) drawPanel.getLayout();

        if (Controller.getUserType() == UserDescription.UserType.SuperAdmin) {

            refreshContestsList();
            setColumn(cur, 3, 122, 1);
            setColumn(cur, 4, 4, 0);
            setColumn(cur, 5, 2, 0);
            setColumn(cur, 6, 4, 0);

        } else if (Controller.getUserType() == UserDescription.UserType.ContestAdmin) {

            setColumn(cur, 3, 0, 0);
            setColumn(cur, 4, 0, 0);
            setColumn(cur, 5, 0, 0);
            setColumn(cur, 6, 0, 0);

            fillDaFormWithData(Controller.getContestID(), RequestResponseUtils.extractFieldNames(Controller.getContestDescription().data));

        } else
            throw new IllegalArgumentException();

    }

    public void deactivate() {
        //do nothing
    }

    private void fillDaFormWithData(int contestID, String[] keys) {

        if (newBean == null) throw new AssertionError("asd");

        usersListModel.clear();

        if (contestID != -1) {
            UserDescription[] ud = Controller.getUsers(contestID);

            for (UserDescription userDescription : ud)
                usersListModel.addElement(new UsersListBean(userDescription));

            userDataTable.setKeys(keys);

            switch (newBean.getType()) {
            case ContestAdmin:
                contestAdminCB.setSelected(true);
                break;
            case SuperAdmin:
                superAdminCB.setSelected(true);
                break;
            case Participant:
                participantCB.setSelected(true);
                break;
            }

            usernameField.setText(newBean.getLogin());
            passwordField.setText("");

            userDataTable.setValues(newBean.getValues());
        } else {
            contestAdminCB.setSelected(false);
            superAdminCB.setSelected(false);
            participantCB.setSelected(false);
            usernameField.setText("");
            passwordField.setText("");           
            userDataTable.setKeys(new String[0]);
        }
    }

    private void createUIComponents() {
        drawPanel = this;
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
        drawPanel.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:115dlu:grow,left:4dlu:noGrow,fill:1dlu:noGrow,left:4dlu:noGrow,fill:115dlu:grow,left:4dlu:noGrow,left:90dlu:grow(2.0),fill:4dlu:noGrow,fill:4dlu:noGrow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:17dlu:noGrow,top:4dlu:noGrow,center:80dlu:grow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:grow,top:4dlu:noGrow,top:4dlu:noGrow"));
        contestsLabel = new JLabel();
        contestsLabel.setText("Доступные контесты");
        CellConstraints cc = new CellConstraints();
        drawPanel.add(contestsLabel, cc.xy(3, 3, CellConstraints.DEFAULT, CellConstraints.CENTER));
        final JSeparator separator1 = new JSeparator();
        separator1.setOrientation(1);
        drawPanel.add(separator1, cc.xywh(5, 3, 1, 26, CellConstraints.FILL, CellConstraints.FILL));
        final JScrollPane scrollPane1 = new JScrollPane();
        drawPanel.add(scrollPane1, cc.xywh(7, 5, 1, 23, CellConstraints.FILL, CellConstraints.FILL));
        usersList = new JList();
        scrollPane1.setViewportView(usersList);
        final JScrollPane scrollPane2 = new JScrollPane();
        drawPanel.add(scrollPane2, cc.xywh(3, 5, 1, 23, CellConstraints.FILL, CellConstraints.FILL));
        contestsList = new JList();
        scrollPane2.setViewportView(contestsList);
        final JLabel label1 = new JLabel();
        label1.setText("Пользователи");
        drawPanel.add(label1, cc.xy(7, 3));
        final JLabel label2 = new JLabel();
        label2.setText("Логин");
        label2.setVerticalAlignment(3);
        label2.setVerticalTextPosition(3);
        drawPanel.add(label2, cc.xy(9, 5, CellConstraints.DEFAULT, CellConstraints.FILL));
        usernameField = new JTextField();
        usernameField.setText("");
        drawPanel.add(usernameField, cc.xy(9, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label3 = new JLabel();
        label3.setText("Пароль");
        label3.setVerticalAlignment(3);
        label3.setVerticalTextPosition(0);
        drawPanel.add(label3, cc.xy(9, 9, CellConstraints.DEFAULT, CellConstraints.FILL));
        passwordField = new JPasswordField();
        passwordField.setText("");
        drawPanel.add(passwordField, cc.xy(9, 11, CellConstraints.FILL, CellConstraints.DEFAULT));
        superAdminCB = new JRadioButton();
        superAdminCB.setText("Администратор");
        drawPanel.add(superAdminCB, cc.xy(9, 13));
        contestAdminCB = new JRadioButton();
        contestAdminCB.setHorizontalAlignment(10);
        contestAdminCB.setHorizontalTextPosition(11);
        contestAdminCB.setText("Администратор контеста");
        drawPanel.add(contestAdminCB, cc.xy(9, 15));
        participantCB = new JRadioButton();
        participantCB.setSelected(false);
        participantCB.setText("Участник");
        drawPanel.add(participantCB, cc.xy(9, 17));
        addButton = new JButton();
        addButton.setText("Добавить");
        drawPanel.add(addButton, cc.xy(9, 21, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JScrollPane scrollPane3 = new JScrollPane();
        drawPanel.add(scrollPane3, cc.xy(9, 19, CellConstraints.FILL, CellConstraints.FILL));
        userDataTable = new JUserTable();
        userDataTable.putClientProperty("JTable.autoStartsEdit", Boolean.TRUE);
        userDataTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        scrollPane3.setViewportView(userDataTable);
        changeButton = new JButton();
        changeButton.setText("Изменить");
        drawPanel.add(changeButton, cc.xy(9, 23, CellConstraints.FILL, CellConstraints.DEFAULT));
        deleteButton = new JButton();
        deleteButton.setText("Удалить");
        drawPanel.add(deleteButton, cc.xy(9, 25, CellConstraints.FILL, CellConstraints.DEFAULT));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(contestAdminCB);
        buttonGroup.add(contestAdminCB);
        buttonGroup.add(participantCB);
        buttonGroup.add(superAdminCB);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return drawPanel;
    }
}
