package ru.ipo.dces.client;

import java.awt.event.*;

import javax.swing.*;

import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.pluginapi.*;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.*;

public class LoginPlugin extends Plugin {

  private static final long serialVersionUID = 5381795243066178246L;

  public final JButton      reloadButton;
  public JList              contestList;

  /**
   * Create the panel
   */
  public LoginPlugin(PluginEnvironment env, final ClientDialog clientDialog) {
    super(env);
    setLayout(new FormLayout(new ColumnSpec[] { FormFactory.DEFAULT_COLSPEC,
        FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("49dlu:grow(2.0)"),
        FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC },
        new RowSpec[] { FormFactory.DEFAULT_ROWSPEC,
            FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
            FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
            FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default") }));

    add(new JLabel("Contests:"), new CellConstraints(1, 1));

    contestList = new JList();
    add(contestList, new CellConstraints(3, 1));
    reloadContest();

    // ���� ��� ����� Login'�
    add(new JLabel("Login:"), new CellConstraints(1, 3));
    final JTextField login = new JTextField();
    add(login, new CellConstraints(3, 3));

    // ���� ��� ����� ������
    add(new JLabel("Password:"), new CellConstraints(1, 5));
    final JPasswordField password = new JPasswordField();
    add(password, new CellConstraints(3, 5));

    reloadButton = new JButton();
    reloadButton.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent arg0) {
        reloadContest();
      }
    });
    reloadButton.setText("Reload");
    add(reloadButton, new CellConstraints(5, 1));

    setName("Admin panel");

    env.setTitle("Login panel");

    final JButton button = new JButton();
    button.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent ae) {
        try {
          ConnectToContestRequest request = new ConnectToContestRequest();
          request.login = login.getText();
          // TODO improve the security here
          request.password = new String(password.getPassword());
          ConnectToContestResponse res = ClientData.server.doRequest(request);
          ClientData.sessionID = res.sessionID;

          // ������� ��� ���������� Plugin'�
          clientDialog.removeAllPlugins();

          // ���� ������������ ������������� ��� ������������� �������

          // �������� ������ � �������
          GetContestDataRequest rq = new GetContestDataRequest();
          GetContestDataResponse rs = ClientData.server.doRequest(rq);
          for (ProblemDescription pd : rs.problems)
            clientDialog.addPlugin(pd.clientPluginID);

          // ��������� Plugin ������ �� �������� � ����� �����
          PluginEnvironmentImpl pe = clientDialog.createPluginEnv();
          clientDialog.addPluginToForm(pe, new LogoutPlugin(pe, clientDialog));

        } catch (Exception e) {
          JOptionPane.showMessageDialog(null, e.getMessage(), "������",
              JOptionPane.ERROR_MESSAGE);
        }
      }
    });
    button.setText("�������������� � ��������!");
    add(button, new CellConstraints(3, 7));
  }

  // �������� ���� � ���������
  // �������� ���� � AdminPlugin
  // ������� ��� ���� ������ -

  private void reloadContest() {
    try {
      AvailableContestsResponse res = ClientData.server
          .doRequest(new AvailableContestsRequest());
      contestList.setListData(res.contests);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, e.getMessage(), "������",
          JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
    }
  }

}
