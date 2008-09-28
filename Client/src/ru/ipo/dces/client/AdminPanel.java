package ru.ipo.dces.client;

import javax.swing.*;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.*;

public class AdminPanel extends JPanel {

  /**
   * Create the panel
   */
  public AdminPanel() {
    super();
    setLayout(new FormLayout(new ColumnSpec[] { FormFactory.DEFAULT_COLSPEC,
        FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC },
        new RowSpec[] { FormFactory.DEFAULT_ROWSPEC,
            FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
            FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default") }));

    final JLabel contestsLabel = new JLabel();
    contestsLabel.setText("Contests:");
    add(contestsLabel, new CellConstraints(1, 1));

    final JList contestList = new JList();
    add(contestList, new CellConstraints(3, 1));
    JLabel l = new JLabel();
    l.setText("sssss");
    contestList.add("11", l);

    final JLabel loginLabel = new JLabel();
    loginLabel.setText("Login:");
    add(loginLabel, new CellConstraints(1, 3));

    final JLabel passwordLabel = new JLabel();
    passwordLabel.setText("Password:");
    add(passwordLabel, new CellConstraints(1, 5));
    //
  }

}
