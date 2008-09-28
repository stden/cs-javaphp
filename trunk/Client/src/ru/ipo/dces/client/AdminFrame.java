package ru.ipo.dces.client;

import java.awt.EventQueue;

import javax.swing.*;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.*;

public class AdminFrame extends JFrame {

  /**
   * Launch the application
   * 
   * @param args
   */
  public static void main(String args[]) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          AdminFrame frame = new AdminFrame();
          frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the frame
   */
  public AdminFrame() {
    super();
    getContentPane()
        .setLayout(
            new FormLayout(new ColumnSpec[] { FormFactory.DEFAULT_COLSPEC,
                FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC },
                new RowSpec[] { RowSpec.decode("default"),
                    FormFactory.RELATED_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.RELATED_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.RELATED_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.RELATED_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.RELATED_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.RELATED_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC }));
    setBounds(100, 100, 500, 375);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    final JLabel loginLabel = new JLabel();
    loginLabel.setText("Login:");
    getContentPane().add(loginLabel, new CellConstraints(1, 3));

    final JTextField textField = new JTextField();
    getContentPane().add(textField, new CellConstraints(3, 3));

    final JTextField textField_1 = new JTextField();
    textField_1.setText("444444");
    getContentPane().add(textField_1, new CellConstraints(3, 5));

    final JLabel passwordLabel = new JLabel();
    passwordLabel.setText("Password:");
    getContentPane().add(passwordLabel, new CellConstraints(1, 5));

    final JLabel contestsLabel = new JLabel();
    contestsLabel.setText("Contests:");
    getContentPane().add(contestsLabel, new CellConstraints());

    final JList contestList = new JList();
    getContentPane().add(contestList, new CellConstraints(3, 1));
    //
  }

}
