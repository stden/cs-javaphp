package ru.ipo.dces.client;

import java.awt.event.*;

import javax.swing.*;

import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.pluginapi.*;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.*;

public class AdminPanel extends Plugin {

  public final JButton reloadButton;
  public JList         contestList;

  /**
   * Create the panel
   */
  public AdminPanel(Client client) {
    super(client);
    setLayout(new FormLayout(new ColumnSpec[] { FormFactory.DEFAULT_COLSPEC,
        FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("49dlu:grow(2.0)"),
        FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC },
        new RowSpec[] { FormFactory.DEFAULT_ROWSPEC,
            FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
            FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
            FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default") }));

    final JLabel contestsLabel = new JLabel();
    contestsLabel.setText("Contests:");
    add(contestsLabel, new CellConstraints(1, 1));

    contestList = new JList();
    add(contestList, new CellConstraints(3, 1));
    reloadContest();

    final JLabel loginLabel = new JLabel();
    loginLabel.setText("Login:");
    add(loginLabel, new CellConstraints(1, 3));

    final JLabel passwordLabel = new JLabel();
    passwordLabel.setText("Password:");
    add(passwordLabel, new CellConstraints(1, 5));

    final JTextField login = new JTextField();
    add(login, new CellConstraints(3, 3));

    final JTextField password = new JTextField();
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

    final JButton button = new JButton();
    button.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent e) {

      }
    });
    button.setText("Присоединиться!");
    add(button, new CellConstraints(3, 7));
    //
  }

  private void reloadContest() {
    try {
      AvailableContestsResponse res = ClientData.server
          .doRequest(new AvailableContestsRequest());
      contestList.setListData(res.contests);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка",
          JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
    }
  }

}
