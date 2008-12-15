package ru.ipo.dces.plugins.admin;

import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.pluginapi.PluginEnvironment;
import ru.ipo.dces.client.Controller;
import ru.ipo.dces.clientservercommunication.ContestDescription;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Илья
 * Date: 06.12.2008
 * Time: 15:20:38
 */

public class LoginPluginV2 extends Plugin {
  private JPanel loginPanel;
  private JButton refreshContestsButton;
  private JPasswordField passwordEdit;
  private JTextField loginEdit;
  private JCheckBox loginAsAdminCheckBox;
  private JList contestsList;
  private JButton registerToContestButton;
  private JButton loginButton;
  private JTable registerToContestTable;
  private JTextArea contestDescriptionTextArea;

  private DefaultListModel contestsListModel = new DefaultListModel();

  private final DefaultTableModel noRegistrationTableModel;
  private final DefaultTableModel defaultTableModel;

  /**
   * Инициализация plugin'а
   *
   * @param env environment for the plugin
   */

  public LoginPluginV2(PluginEnvironment env) {
    super(env);
    env.setTitle("Контесты");

    //set list model for contest list
    $$$setupUI$$$();
    contestsList.setModel(contestsListModel);

    //set all listeners
    setListeners();

    //show contests
    refreshContests();

    //set table models
    noRegistrationTableModel
            = new DefaultTableModel(new Object[][]{}, new Object[]{"Самостоятельная регистрация невозможна"});

    defaultTableModel = new DefaultTableModel();

    registerToContestTable.setModel(noRegistrationTableModel);

    //set default login/pass
    loginEdit.setText("admin");
    passwordEdit.setText("pass");
    loginAsAdminCheckBox.setSelected(true);
  }

  private void setListeners() {
    refreshContestsButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        refreshContests();
      }
    });

    contestsList.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        ContestListBean bean = (ContestListBean) contestsList.getSelectedValue();
        if (bean == null) {
          //clear contents of all controls
          passwordEdit.setText("");
          loginEdit.setText("");
          loginAsAdminCheckBox.setSelected(false);
          contestDescriptionTextArea.setText("");
          registerToContestTable.setModel(noRegistrationTableModel);
        } else {
          passwordEdit.setText("");
          loginEdit.setText("");
          loginAsAdminCheckBox.setSelected(false);
          contestDescriptionTextArea.setText(bean.contestDescription.description);

          if (bean.contestDescription.registrationType == ContestDescription.RegistrationType.Self) {
            registerToContestTable.setModel(defaultTableModel);

            defaultTableModel.setColumnCount(0);
            for (String item : bean.contestDescription.data)
              defaultTableModel.addColumn(item, new Object[]{""});
          } else {
            registerToContestTable.setModel(noRegistrationTableModel);
          }
        }
      }
    });

    loginButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        int contestID;

        //get contest id
        if (loginAsAdminCheckBox.isSelected()) {
          contestID = 0;
        } else {
          ContestListBean bean = (ContestListBean) contestsList.getSelectedValue();
          if (bean == null) return;
          contestID = bean.contestDescription.contestID;
        }

        Controller.login(loginEdit.getText(), passwordEdit.getPassword(), contestID);
      }
    });

    registerToContestButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ContestListBean bean = (ContestListBean) contestsList.getSelectedValue();
        if (bean == null) return;

        //TODO confirm the operation

        //get user data
        if (registerToContestTable.getModel() == noRegistrationTableModel) return;
        String[] userData = new String[bean.contestDescription.data.length];
        for (int i = 0; i < bean.contestDescription.data.length; i++)
          userData[i] = defaultTableModel.getValueAt(0, i).toString();

        Controller.registerAnonymouslyToContest(loginEdit.getText(), passwordEdit.getPassword(), bean.contestDescription.contestID, userData);

        //TODO if succeeded, remove all data from table
      }
    });
  }

  private void refreshContests() {
    contestsListModel.clear();

    final ContestDescription[] contestDescriptions = Controller.getAvailableContests();
    for (ContestDescription cd : contestDescriptions)
      contestsListModel.addElement(new ContestListBean(cd));

    contestsList.setSelectedIndex(-1);
  }

  private void createUIComponents() {
    loginPanel = this;
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
    loginPanel.setLayout(new FormLayout("fill:0dlu:grow,left:4dlu:noGrow,fill:32dlu:noGrow,left:4dlu:noGrow,fill:60dlu:noGrow,left:4dlu:noGrow,fill:58dlu:noGrow,left:4dlu:noGrow,fill:106dlu:noGrow,left:4dlu:noGrow,fill:64dlu:noGrow,left:4dlu:noGrow,left:0dlu:grow", "center:max(d;0px):noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:48dlu:noGrow,top:4dlu:noGrow,center:32dlu:noGrow,top:4dlu:noGrow,center:2dlu:noGrow,top:4dlu:noGrow,center:12dlu:noGrow,top:4dlu:noGrow,center:12dlu:noGrow,top:4dlu:noGrow,center:12dlu:noGrow,top:4dlu:noGrow,center:2dlu:noGrow,top:5dlu:noGrow,center:48dlu:noGrow,top:4dlu:noGrow,center:12dlu:noGrow,top:4dlu:noGrow"));
    final JLabel label1 = new JLabel();
    label1.setHorizontalAlignment(4);
    label1.setHorizontalTextPosition(11);
    label1.setText("Логин");
    CellConstraints cc = new CellConstraints();
    loginPanel.add(label1, cc.xy(3, 13, CellConstraints.FILL, CellConstraints.DEFAULT));
    final JLabel label2 = new JLabel();
    label2.setHorizontalAlignment(4);
    label2.setHorizontalTextPosition(11);
    label2.setText("Пароль");
    loginPanel.add(label2, cc.xy(3, 15));
    passwordEdit = new JPasswordField();
    loginPanel.add(passwordEdit, cc.xyw(5, 15, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
    loginEdit = new JTextField();
    loginPanel.add(loginEdit, cc.xyw(5, 13, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
    final JScrollPane scrollPane1 = new JScrollPane();
    loginPanel.add(scrollPane1, cc.xywh(3, 5, 5, 5, CellConstraints.FILL, CellConstraints.FILL));
    contestsList = new JList();
    final DefaultListModel defaultListModel1 = new DefaultListModel();
    contestsList.setModel(defaultListModel1);
    scrollPane1.setViewportView(contestsList);
    final JScrollPane scrollPane2 = new JScrollPane();
    loginPanel.add(scrollPane2, cc.xyw(3, 21, 7, CellConstraints.FILL, CellConstraints.FILL));
    registerToContestTable = new JTable();
    registerToContestTable.putClientProperty("JTable.autoStartsEdit", Boolean.TRUE);
    registerToContestTable.putClientProperty("Table.isFileList", Boolean.FALSE);
    registerToContestTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    scrollPane2.setViewportView(registerToContestTable);
    final JLabel label3 = new JLabel();
    label3.setText("Список доступных контестов");
    loginPanel.add(label3, cc.xyw(3, 3, 3));
    final JLabel label4 = new JLabel();
    label4.setText("Регистрация для участния в контесте");
    loginPanel.add(label4, cc.xyw(3, 17, 3));
    loginAsAdminCheckBox = new JCheckBox();
    loginAsAdminCheckBox.setHorizontalAlignment(4);
    loginAsAdminCheckBox.setText("Администратор");
    loginPanel.add(loginAsAdminCheckBox, cc.xyw(3, 23, 9));
    final JLabel label5 = new JLabel();
    label5.setText("Описание контеста");
    loginPanel.add(label5, cc.xyw(9, 5, 3));
    refreshContestsButton = new JButton();
    refreshContestsButton.setText("Обновить");
    loginPanel.add(refreshContestsButton, cc.xy(7, 3, CellConstraints.DEFAULT, CellConstraints.TOP));
    loginButton = new JButton();
    loginButton.setText("Войти");
    loginPanel.add(loginButton, cc.xywh(11, 13, 1, 3, CellConstraints.FILL, CellConstraints.TOP));
    registerToContestButton = new JButton();
    registerToContestButton.setText("Регистрация");
    loginPanel.add(registerToContestButton, cc.xy(11, 21, CellConstraints.FILL, CellConstraints.TOP));
    final JScrollPane scrollPane3 = new JScrollPane();
    loginPanel.add(scrollPane3, cc.xyw(9, 7, 3, CellConstraints.FILL, CellConstraints.FILL));
    contestDescriptionTextArea = new JTextArea();
    contestDescriptionTextArea.setEditable(false);
    contestDescriptionTextArea.setFont(UIManager.getFont("List.font"));
    contestDescriptionTextArea.setLineWrap(true);
    scrollPane3.setViewportView(contestDescriptionTextArea);
    final JSeparator separator1 = new JSeparator();
    loginPanel.add(separator1, cc.xyw(2, 11, 11, CellConstraints.FILL, CellConstraints.FILL));
    final JSeparator separator2 = new JSeparator();
    loginPanel.add(separator2, cc.xyw(2, 19, 11, CellConstraints.FILL, CellConstraints.FILL));
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return loginPanel;
  }

  private class ContestListBean {
    public final ContestDescription contestDescription;

    private ContestListBean(ContestDescription contestDescription) {
      this.contestDescription = contestDescription;
    }

    public String toString() {
      return contestDescription.name;
    }

  }

}
