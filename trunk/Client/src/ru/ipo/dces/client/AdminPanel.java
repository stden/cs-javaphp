package ru.ipo.dces.client;

import javax.swing.*;

import ru.ipo.dces.pluginapi.*;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.*;

public class AdminPanel extends JPanel implements Plugin {

  /**
   * Create the panel
   */
  public AdminPanel() {
    super();
    setLayout(new FormLayout(new ColumnSpec[] { FormFactory.DEFAULT_COLSPEC,
        FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("49dlu") },
        new RowSpec[] { FormFactory.DEFAULT_ROWSPEC,
            FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
            FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC }));

    final JLabel contestsLabel = new JLabel();
    contestsLabel.setText("Contests:");
    add(contestsLabel, new CellConstraints(1, 1));

    final JList contestList = new JList();
    contestList.setListData(new Integer[] { 1, 2 });
    add(contestList, new CellConstraints(3, 1));

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
    //
  }

  @Override
  public int getVersion() {
    return 1;
  }

  @Override
  public void initialize(Client client, JPanel panel) {
    // TODO Auto-generated method stub

  }

}
