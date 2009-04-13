package ru.ipo.dces.plugins.admin;

import ru.ipo.dces.pluginapi.PluginEnvironment;
import ru.ipo.dces.client.Controller;
import ru.ipo.dces.client.RequestResponseUtils;
import ru.ipo.dces.client.AdminPlugin;
import ru.ipo.dces.client.Localization;
import ru.ipo.dces.clientservercommunication.ContestDescription;
import ru.ipo.dces.clientservercommunication.UserDataField;
import ru.ipo.dces.plugins.admin.beans.ContestsListBean;

import javax.swing.*;
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

public class LoginPluginV2 extends JPanel implements AdminPlugin {
  private JPanel loginPanel;
  private JButton refreshContestsButton;
  private JPasswordField passwordEdit;
  private JTextField loginEdit;
  private JCheckBox loginAsAdminCheckBox;
  private JList contestsList;
  private JButton registerToContestButton;
  private JButton loginButton;
  private JUserTable registerToContestTable;
  private JTextPane contestDescriptionTextPane;

  private DefaultListModel contestsListModel = new DefaultListModel();

  /**
   * Инициализация plugin'а
   *
   * @param env environment for the plugin
   */

  public LoginPluginV2(PluginEnvironment env) {
    env.setTitle(Localization.getAdminPluginName(LoginPluginV2.class));

    //set list model for contest list
    $$$setupUI$$$();
    contestsList.setModel(contestsListModel);

    //set all listeners
    setListeners();

    //show contests
    refreshContests();

    //set table models
    setNoSelfRegistration();

    //set default login/pass
    loginEdit.setText("admin");
    passwordEdit.setText("pass");
    loginAsAdminCheckBox.setSelected(true);
  }

  private void setNoSelfRegistration() {
    registerToContestTable.setData(new String[0], new String[0]);
    registerToContestButton.setEnabled(false);
  }

  private void setSelfRegistration(UserDataField[] data) {
    registerToContestTable.setKeys(RequestResponseUtils.extractFieldNames(data));
    registerToContestButton.setEnabled(true);
  }

  private void setListeners() {
    refreshContestsButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        refreshContests();
      }
    });

    contestsList.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        ContestsListBean bean = (ContestsListBean) contestsList.getSelectedValue();
        if (bean == null) {
          //clear contents of all controls
          passwordEdit.setText("");
          loginEdit.setText("");
          loginAsAdminCheckBox.setSelected(false);
          contestDescriptionTextPane.setText("");
          setNoSelfRegistration();
        } else {
          passwordEdit.setText("");
          loginEdit.setText("");
          loginAsAdminCheckBox.setSelected(false);
          contestDescriptionTextPane.setText(bean.getDescription().description);

          if (bean.getDescription().registrationType == ContestDescription.RegistrationType.Self) {
            setSelfRegistration(bean.getDescription().data);
          } else {
            setNoSelfRegistration();
          }
        }
      }
    });

    loginButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ContestDescription contest = new ContestDescription();

        //get contest id
        if (loginAsAdminCheckBox.isSelected()) {
          contest.contestID = 0;
        } else {
          ContestsListBean bean = (ContestsListBean) contestsList.getSelectedValue();
          if (bean == null) return;
          contest = bean.getDescription();
        }

        Controller.login(loginEdit.getText(), passwordEdit.getPassword(), contest);
      }
    });

    registerToContestButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ContestsListBean bean = (ContestsListBean) contestsList.getSelectedValue();
        if (bean == null) return;

        final String login = loginEdit.getText();
        if (login.equals("")) {
          JOptionPane.showMessageDialog(null, "Укажите логин для регистрации");
          return;
        }

        if (JOptionPane.showConfirmDialog(
                null,
                "Подтвердите регистрацию участника с логином " + login,
                "Регистрация",
                JOptionPane.YES_NO_OPTION
        ) == JOptionPane.NO_OPTION) return;

        //get user data
        UserDataField[] contestData = bean.getDescription().data;
        String[] userData = new String[contestData.length];
        for (int i = 0; i < contestData.length; i++)
          userData[i] = registerToContestTable.getValue(i);

        Controller.registerAnonymouslyToContest(login, passwordEdit.getPassword(), bean.getDescription().contestID, userData);

        //clear values
        registerToContestTable.setKeys(RequestResponseUtils.extractFieldNames(contestData));
      }
    });
  }

  private void refreshContests() {
    contestsListModel.clear();

    final ContestDescription[] contestDescriptions = Controller.getAvailableContests();
    for (ContestDescription cd : contestDescriptions)
      contestsListModel.addElement(new ContestsListBean(cd));

    contestsList.setSelectedIndex(-1);
  }

  private void createUIComponents() {
    loginPanel = this;
  }

  public JPanel getPanel() {
    return this;
  }

  public void activate() {
    //do nothing
  }

  public void deactivate() {
    //do nothing
  }

  public void contestSelected(ContestDescription contest) {
    //To change body of implemented methods use File | Settings | File Templates.
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
    loginPanel.setLayout(new FormLayout("fill:0dlu:grow,left:4dlu:noGrow,fill:100dlu:noGrow,left:4dlu:noGrow,fill:58dlu:noGrow,left:4dlu:noGrow,fill:2dlu:noGrow,left:4dlu:noGrow,fill:26dlu:noGrow,left:4dlu:noGrow,fill:120dlu:noGrow,left:4dlu:noGrow,left:0dlu:grow", "center:max(d;0px):noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:2px:noGrow,top:4dlu:noGrow,center:12dlu:noGrow,top:4dlu:noGrow,center:20dlu:noGrow,top:4dlu:noGrow,center:2dlu:noGrow,top:4dlu:noGrow,center:16px:noGrow,top:4dlu:noGrow,top:60dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:4dlu:noGrow"));
    final JScrollPane scrollPane1 = new JScrollPane();
    CellConstraints cc = new CellConstraints();
    loginPanel.add(scrollPane1, cc.xywh(3, 5, 3, 13, CellConstraints.FILL, CellConstraints.FILL));
    contestsList = new JList();
    final DefaultListModel defaultListModel1 = new DefaultListModel();
    contestsList.setModel(defaultListModel1);
    scrollPane1.setViewportView(contestsList);
    final JLabel label1 = new JLabel();
    label1.setText("Список доступных контестов");
    loginPanel.add(label1, cc.xyw(3, 3, 2));
    refreshContestsButton = new JButton();
    refreshContestsButton.setText("Обновить");
    loginPanel.add(refreshContestsButton, cc.xy(5, 3, CellConstraints.DEFAULT, CellConstraints.TOP));
    final JScrollPane scrollPane2 = new JScrollPane();
    loginPanel.add(scrollPane2, cc.xywh(9, 17, 3, 7, CellConstraints.FILL, CellConstraints.FILL));
    registerToContestTable = new JUserTable();
    registerToContestTable.putClientProperty("JTable.autoStartsEdit", Boolean.TRUE);
    registerToContestTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    scrollPane2.setViewportView(registerToContestTable);
    final JLabel label2 = new JLabel();
    label2.setHorizontalAlignment(4);
    label2.setHorizontalTextPosition(11);
    label2.setText("Логин");
    loginPanel.add(label2, cc.xy(9, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
    final JLabel label3 = new JLabel();
    label3.setHorizontalAlignment(4);
    label3.setHorizontalTextPosition(11);
    label3.setText("Пароль");
    loginPanel.add(label3, cc.xy(9, 7));
    loginEdit = new JTextField();
    loginPanel.add(loginEdit, cc.xy(11, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
    passwordEdit = new JPasswordField();
    loginPanel.add(passwordEdit, cc.xy(11, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
    final JSeparator separator1 = new JSeparator();
    loginPanel.add(separator1, cc.xyw(9, 13, 4, CellConstraints.FILL, CellConstraints.FILL));
    final JLabel label4 = new JLabel();
    label4.setText("Регистрация для участния в контесте");
    loginPanel.add(label4, cc.xyw(9, 15, 3));
    final JSeparator separator2 = new JSeparator();
    loginPanel.add(separator2, cc.xyw(3, 19, 3, CellConstraints.FILL, CellConstraints.FILL));
    final JLabel label5 = new JLabel();
    label5.setText("Описание контеста");
    loginPanel.add(label5, cc.xyw(3, 21, 3));
    loginButton = new JButton();
    loginButton.setText("Войти");
    loginPanel.add(loginButton, cc.xy(11, 9, CellConstraints.FILL, CellConstraints.TOP));
    registerToContestButton = new JButton();
    registerToContestButton.setText("Регистрация");
    loginPanel.add(registerToContestButton, cc.xy(11, 25, CellConstraints.FILL, CellConstraints.TOP));
    final JScrollPane scrollPane3 = new JScrollPane();
    loginPanel.add(scrollPane3, cc.xywh(3, 23, 3, 3, CellConstraints.FILL, CellConstraints.FILL));
    contestDescriptionTextPane = new JTextPane();
    scrollPane3.setViewportView(contestDescriptionTextPane);
    loginAsAdminCheckBox = new JCheckBox();
    loginAsAdminCheckBox.setHorizontalAlignment(4);
    loginAsAdminCheckBox.setText("Администратор");
    loginPanel.add(loginAsAdminCheckBox, cc.xy(11, 11, CellConstraints.RIGHT, CellConstraints.DEFAULT));
    final JSeparator separator3 = new JSeparator();
    separator3.setOrientation(1);
    loginPanel.add(separator3, cc.xywh(7, 3, 1, 23, CellConstraints.FILL, CellConstraints.FILL));
    label5.setLabelFor(contestDescriptionTextPane);
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return loginPanel;
  }
}
