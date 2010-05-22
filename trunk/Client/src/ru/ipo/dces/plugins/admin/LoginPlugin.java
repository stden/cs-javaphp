package ru.ipo.dces.plugins.admin;

import ru.ipo.dces.pluginapi.PluginEnvironment;
import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.client.*;
import ru.ipo.dces.client.ContestConnection;
import ru.ipo.dces.client.components.ContestChoosingPanel;
import ru.ipo.dces.clientservercommunication.ContestDescription;
import ru.ipo.dces.clientservercommunication.UserDataField;
import ru.ipo.dces.log.LogMessageType;
import ru.ipo.dces.exceptions.ServerReturnedError;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;

import javax.swing.*;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;
import ru.ipo.dces.trash.CreateContestPlugin;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.text.DateFormat;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Илья
 * Date: 06.12.2008
 * Time: 15:20:38
 */

public class
        LoginPlugin extends JPanel implements Plugin {
  private JPanel loginPanel;
  private JPasswordField passwordEdit;
  private JTextField loginEdit;
  private JCheckBox loginAsAdminCheckBox;
  private JButton registerToContestButton;
  private JButton loginButton;
  private UserTable registerToContestTable;
  private JTextPane contestDescriptionTextPane;
  private ContestChoosingPanel contestChoosingPanel;

  /**
   * Инициализация plugin'а
   *
   * @param env environment for the plugin
   */

  public LoginPlugin(PluginEnvironment env) {
    env.setTitle(Localization.getAdminPluginName(LoginPlugin.class));

    //set list model for contest list
    $$$setupUI$$$();

    //set all listeners
    setListeners();

    //show contests
    //contestChoosingPanel.refreshContestList(); //will be done by ativate() method

    //set table models
    setNoSelfRegistration();

    //set default login/pass
    
    //loginEdit.setText("admin");
    //passwordEdit.setText("pass");
    //loginAsAdminCheckBox.setSelected(true);
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
    contestChoosingPanel.addContestChangedActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        ContestDescription cd = contestChoosingPanel.getContest();

        if (cd == null) {
          //clear contents of all controls
          passwordEdit.setText("");
          loginEdit.setText("");
          loginAsAdminCheckBox.setSelected(false);
          contestDescriptionTextPane.setText("");
          setNoSelfRegistration();
          return;
        }

        passwordEdit.setText("");
        loginEdit.setText("");
        loginAsAdminCheckBox.setSelected(false);
        contestDescriptionTextPane.setText(cd.description);

        if (cd.registrationType == ContestDescription.RegistrationType.Self) {
          setSelfRegistration(cd.data);
        } else {
          setNoSelfRegistration();
        }
      }
    });

    loginButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ContestDescription contest;

        //get contest id
        if (loginAsAdminCheckBox.isSelected()) {
          contest = new ContestDescription();
          contest.contestID = 0;
        } else {
          contest = contestChoosingPanel.getContest();
          if (contest == null) return;
        }

        login(loginEdit.getText(), passwordEdit.getPassword(), contest);
      }
    });

    registerToContestButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ContestDescription cd = contestChoosingPanel.getContest();
        if (cd == null) return;

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
        UserDataField[] contestData = cd.data;
        String[] userData = new String[contestData.length];
        for (int i = 0; i < contestData.length; i++)
          userData[i] = registerToContestTable.getValue(i);

        Controller.registerAnonymouslyToContest(login, passwordEdit.getPassword(), cd.contestID, userData);

        //clear values
        registerToContestTable.setKeys(RequestResponseUtils.extractFieldNames(contestData));
      }
    });
  }

  private void createUIComponents() {
    loginPanel = this;
  }

  public JPanel getPanel() {
    return this;
  }

  public void activate() {
    contestChoosingPanel.refreshContestList();
  }

  public void deactivate() {
    //do nothing
  }

  public static void login(String login, char[] password, ContestDescription contest) {
    try {
      Controller.connectToContest(contest, login, password);

      //show message like: 'ok, connected, finish is at...'
      String leftTimeMessage;
      ContestConnection con = Controller.getContestConnection();
      switch (con.getUser().userType) {
        case SuperAdmin:
          leftTimeMessage = "Суперадминистратор";
          break;
        case ContestAdmin:
          leftTimeMessage = "Администратор соревнования";
          break;
        case Participant:
          Date now = new Date();
          if (now.before(con.getFinishTime()))
            //TODO format only time if the finish is today
            leftTimeMessage = "Окончание соревнования в " + DateFormat.getInstance().format(con.getFinishTime());
          else
            leftTimeMessage = "Соревнование уже закончилось";
          break;
        default:
          leftTimeMessage = "?? Неизвестный тип пользователя";
      }

      Controller.getLogger().log(
              "Успешное подключение к соревнованию. " + leftTimeMessage,
              LogMessageType.OK,
              Localization.LOGGER_NAME
      );

      // Удаляем все запущенные Plugin'ы
      Controller.getClientDialog().clearLeftPanel();

      // Если пользователь администратор или администратор сервера
      // загружаем ему административные Plugin'ы
      switch (con.getUser().userType) {
        case ContestAdmin:
          Controller.addAdminPlugin(ContestPluginV2.class);
          Controller.addAdminPlugin(ManageUsersPlugin.class);
          Controller.addAdminPlugin(ResultsPlugin.class);
          Controller.addAdminPlugin(LogoutPlugin.class);
          break;
        case SuperAdmin:
          Controller.addAdminPlugin(ContestPluginV2.class);
          Controller.addAdminPlugin(CreateContestPlugin.class);
//          Controller.addAdminPlugin(AdjustContestsPlugin.class);
          Controller.addAdminPlugin(ManageUsersPlugin.class);
          Controller.addAdminPlugin(PluginsManagementPlugin.class);
          Controller.addAdminPlugin(ResultsPlugin.class);
          Controller.addAdminPlugin(LogoutPlugin.class);
          break;
        case Participant:
          // Получаем данные о задачах
          Controller.refreshParticipantInfo(false, false);
      }

    } catch (ServerReturnedError e) {
      Controller.log(e);
      Controller.getClientDialog().initialState();
    } catch (GeneralRequestFailureException e) {
      //log nothing
      Controller.getClientDialog().initialState();
    } catch (IOException e) {
      Controller.getLogger().log("Не удалось загрузить плагины", LogMessageType.Error, Localization.LOGGER_NAME);
      Controller.getClientDialog().initialState();
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
    loginPanel.setLayout(new FormLayout("fill:0dlu:grow,left:4dlu:noGrow,fill:100dlu:noGrow,left:4dlu:noGrow,fill:58dlu:noGrow,left:4dlu:noGrow,fill:2dlu:noGrow,left:4dlu:noGrow,fill:26dlu:noGrow,left:4dlu:noGrow,fill:120dlu:noGrow,left:4dlu:noGrow,left:0dlu:grow", "center:max(d;0px):noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:2px:noGrow,top:4dlu:noGrow,center:12dlu:noGrow,top:4dlu:noGrow,center:20dlu:noGrow,top:4dlu:noGrow,center:2dlu:noGrow,top:4dlu:noGrow,center:16px:noGrow,top:4dlu:noGrow,top:60dlu:noGrow,top:4dlu:noGrow,center:16dlu:noGrow,top:4dlu:noGrow,center:4dlu:noGrow"));
    final JScrollPane scrollPane1 = new JScrollPane();
    CellConstraints cc = new CellConstraints();
    loginPanel.add(scrollPane1, cc.xywh(9, 17, 3, 7, CellConstraints.FILL, CellConstraints.FILL));
    registerToContestTable = new UserTable();
    registerToContestTable.putClientProperty("JTable.autoStartsEdit", Boolean.TRUE);
    registerToContestTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    scrollPane1.setViewportView(registerToContestTable);
    final JLabel label1 = new JLabel();
    label1.setHorizontalAlignment(4);
    label1.setHorizontalTextPosition(11);
    label1.setText("Логин");
    loginPanel.add(label1, cc.xy(9, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
    final JLabel label2 = new JLabel();
    label2.setHorizontalAlignment(4);
    label2.setHorizontalTextPosition(11);
    label2.setText("Пароль");
    loginPanel.add(label2, cc.xy(9, 7));
    loginEdit = new JTextField();
    loginPanel.add(loginEdit, cc.xy(11, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
    passwordEdit = new JPasswordField();
    loginPanel.add(passwordEdit, cc.xy(11, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
    final JSeparator separator1 = new JSeparator();
    loginPanel.add(separator1, cc.xyw(9, 13, 4, CellConstraints.FILL, CellConstraints.FILL));
    final JLabel label3 = new JLabel();
    label3.setText("Регистрация для участния в контесте");
    loginPanel.add(label3, cc.xyw(9, 15, 3));
    final JSeparator separator2 = new JSeparator();
    loginPanel.add(separator2, cc.xyw(3, 19, 3, CellConstraints.FILL, CellConstraints.FILL));
    final JLabel label4 = new JLabel();
    label4.setText("Описание контеста");
    loginPanel.add(label4, cc.xyw(3, 21, 3));
    loginButton = new JButton();
    loginButton.setText("Войти");
    loginPanel.add(loginButton, cc.xy(11, 9, CellConstraints.FILL, CellConstraints.TOP));
    registerToContestButton = new JButton();
    registerToContestButton.setText("Регистрация");
    loginPanel.add(registerToContestButton, cc.xy(11, 25, CellConstraints.FILL, CellConstraints.TOP));
    final JScrollPane scrollPane2 = new JScrollPane();
    loginPanel.add(scrollPane2, cc.xywh(3, 23, 3, 3, CellConstraints.FILL, CellConstraints.FILL));
    contestDescriptionTextPane = new JTextPane();
    scrollPane2.setViewportView(contestDescriptionTextPane);
    loginAsAdminCheckBox = new JCheckBox();
    loginAsAdminCheckBox.setHorizontalAlignment(4);
    loginAsAdminCheckBox.setText("Администратор");
    loginPanel.add(loginAsAdminCheckBox, cc.xy(11, 11, CellConstraints.RIGHT, CellConstraints.DEFAULT));
    final JSeparator separator3 = new JSeparator();
    separator3.setOrientation(1);
    loginPanel.add(separator3, cc.xywh(7, 3, 1, 23, CellConstraints.FILL, CellConstraints.FILL));
    contestChoosingPanel = new ContestChoosingPanel();
    contestChoosingPanel.setBeforeLabelGap(20);
    contestChoosingPanel.setEnabled(true);
    contestChoosingPanel.setPopup(false);
    contestChoosingPanel.setShowLabel(true);
    loginPanel.add(contestChoosingPanel, cc.xywh(3, 3, 3, 15));
    label4.setLabelFor(contestDescriptionTextPane);
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return loginPanel;
  }
}
