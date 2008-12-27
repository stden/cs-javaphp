package ru.ipo.dces.plugins.admin;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.Sizes;
import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.pluginapi.PluginEnvironment;
import ru.ipo.dces.client.Controller;
import ru.ipo.dces.clientservercommunication.UserDescription;
import ru.ipo.dces.clientservercommunication.ContestDescription;
import ru.ipo.dces.plugins.admin.beans.ContestsListBean;
import ru.ipo.dces.plugins.admin.beans.UsersListBean;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class ManageUsersPlugin extends JPanel implements Plugin {
    private JPanel drawPanel;
    private JList contestsList;
    private JLabel contestsLabel;
    private JList usersList;
    private JButton addButton;
    private JButton deleteButton;
    private JButton changeButton;
    private JUserTable userData;
    private JRadioButton superAdminCB;
    private JRadioButton participantCB;
    private JRadioButton contestAdminCB;
    private JTextField usernameField;
    private JTextField passwordField;
    private JCheckBox showPasswordsCheckBox;
    private DefaultListModel contestsListModel = new DefaultListModel();
    private DefaultListModel usersListModel = new DefaultListModel();


    /**
     * ������������� plugin'�
     *
     * @param env environment for the plugin
     */
    public ManageUsersPlugin(PluginEnvironment env) {

        $$$setupUI$$$();

        env.setTitle("������������");

        contestsList.setModel(contestsListModel);
        usersList.setModel(usersListModel);

        contestsList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                ContestsListBean cb = (ContestsListBean) contestsList.getSelectedValue();

                if (cb == null)
                    return;

                fillDaFormWithData(cb.getDescription().contestID, cb.getDescription().data);
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
        zeroContestDescription.name = "������� �������";
        zeroContestDescription.contestID = 0;
        zeroContestDescription.data = new String[]{};

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
            setColumn(cur, 3, 122, 0);
            setColumn(cur, 4, 4, 1);
            setColumn(cur, 5, 2, 0);
            setColumn(cur, 6, 4, 1);

        } else if (Controller.getUserType() == UserDescription.UserType.ContestAdmin) {

            setColumn(cur, 3, 0, 0);
            setColumn(cur, 4, 0, 0);
            setColumn(cur, 5, 0, 0);
            setColumn(cur, 6, 0, 0);

            fillDaFormWithData(Controller.getContestID(), Controller.getContestDescription().data);

        } else
            throw new IllegalArgumentException();

    }

  public void deactivate() {
    //do nothing
  }

  private void fillDaFormWithData(int contestID, String[] keys) {

        UserDescription[] ud = Controller.getUsers(contestID);

        usersListModel.clear();

        for (UserDescription userDescription : ud)
            usersListModel.addElement(new UsersListBean(userDescription));

        userData.setKeys(keys);

        superAdminCB.setSelected(false);
        contestAdminCB.setSelected(false);
        participantCB.setSelected(false);
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
        drawPanel.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:130dlu:grow(3.0),left:4dlu:noGrow,fill:1dlu:noGrow,left:4dlu:noGrow,fill:100dlu:grow(4.0),left:4dlu:noGrow,left:90dlu:grow,fill:max(d;4px):noGrow,fill:max(d;4px):noGrow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:84dlu:grow,top:4dlu:noGrow,top:1dlu:noGrow,center:4dlu:noGrow,top:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:0dlu:grow,center:max(d;4px):noGrow,top:16dlu:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        contestsLabel = new JLabel();
        contestsLabel.setText("��������� ��������");
        CellConstraints cc = new CellConstraints();
        drawPanel.add(contestsLabel, cc.xy(3, 3, CellConstraints.DEFAULT, CellConstraints.CENTER));
        final JSeparator separator1 = new JSeparator();
        separator1.setOrientation(1);
        drawPanel.add(separator1, cc.xywh(5, 3, 1, 21, CellConstraints.FILL, CellConstraints.FILL));
        final JScrollPane scrollPane1 = new JScrollPane();
        drawPanel.add(scrollPane1, cc.xywh(7, 5, 1, 7, CellConstraints.FILL, CellConstraints.FILL));
        usersList = new JList();
        scrollPane1.setViewportView(usersList);
        final JSeparator separator2 = new JSeparator();
        drawPanel.add(separator2, cc.xyw(7, 13, 3, CellConstraints.FILL, CellConstraints.FILL));
        final JScrollPane scrollPane2 = new JScrollPane();
        drawPanel.add(scrollPane2, cc.xywh(7, 15, 1, 7, CellConstraints.FILL, CellConstraints.FILL));
        userData = new JUserTable();
        userData.putClientProperty("JTable.autoStartsEdit", Boolean.TRUE);
        userData.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        scrollPane2.setViewportView(userData);
        changeButton = new JButton();
        changeButton.setText("��������");
        drawPanel.add(changeButton, cc.xyw(7, 23, 3));
        final JScrollPane scrollPane3 = new JScrollPane();
        drawPanel.add(scrollPane3, cc.xywh(3, 5, 1, 19, CellConstraints.FILL, CellConstraints.FILL));
        contestsList = new JList();
        scrollPane3.setViewportView(contestsList);
        deleteButton = new JButton();
        deleteButton.setText("�������");
        drawPanel.add(deleteButton, cc.xy(9, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
        addButton = new JButton();
        addButton.setText("��������");
        drawPanel.add(addButton, cc.xy(9, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label1 = new JLabel();
        label1.setText("������������");
        drawPanel.add(label1, cc.xy(7, 3));
        superAdminCB = new JRadioButton();
        superAdminCB.setText("�������������");
        drawPanel.add(superAdminCB, cc.xy(9, 15));
        participantCB = new JRadioButton();
        participantCB.setSelected(false);
        participantCB.setText("��������");
        drawPanel.add(participantCB, cc.xy(9, 19));
        contestAdminCB = new JRadioButton();
        contestAdminCB.setHorizontalAlignment(10);
        contestAdminCB.setHorizontalTextPosition(11);
        contestAdminCB.setText("������������� ��������");
        drawPanel.add(contestAdminCB, cc.xy(9, 17));
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
