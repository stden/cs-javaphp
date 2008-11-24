package ru.ipo.dces.plugins;

import java.awt.event.*;

import javax.swing.*;

import ru.ipo.dces.client.Controller;
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
  public LoginPlugin(PluginEnvironment env) {
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
    contestList.setListData(Controller.reloadContest());

    // Поле для ввода Login'а
    add(new JLabel("Login:"), new CellConstraints(1, 3));
    final JTextField login = new JTextField();
    add(login, new CellConstraints(3, 3));

    // Поле для ввода пароля
    add(new JLabel("Password:"), new CellConstraints(1, 5));
    final JPasswordField password = new JPasswordField();
    add(password, new CellConstraints(3, 5));

    login.setText("admin");
    password.setText("pass");

    reloadButton = new JButton();
    reloadButton.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent arg0) {
        contestList.setListData(Controller.reloadContest());
      }
    });
    reloadButton.setText("Reload");
    add(reloadButton, new CellConstraints(5, 1));

    setName("Admin panel");

    env.setTitle("Login panel");

    final JButton button = new JButton();
    button.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent ae) {
        Controller.login(login.getText(), password.getPassword());
      }
    });
    button.setText("Присоединиться к контесту!");
    add(button, new CellConstraints(3, 7));
  }

}
