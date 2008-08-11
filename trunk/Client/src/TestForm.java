import java.awt.event.*;

import javax.swing.*;

import com.jgoodies.forms.layout.*;

/**
 * @author denis stepulenok
 */
public class TestForm extends JPanel {
  // JFormDesigner - Variables declaration - DO NOT MODIFY //GEN-BEGIN:variables
  // Generated using JFormDesigner Evaluation license - denis stepulenok
  private JButton button1;

  // JFormDesigner - End of variables declaration //GEN-END:variables

  public TestForm() {
    initComponents();
  }

  private void button1ActionPerformed(ActionEvent e) {
    // TODO add your code here
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY
    // //GEN-BEGIN:initComponents
    // Generated using JFormDesigner Evaluation license - denis stepulenok
    button1 = new JButton();
    CellConstraints cc = new CellConstraints();

    // ======== this ========

    // JFormDesigner evaluation mark
    setBorder(new javax.swing.border.CompoundBorder(
        new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(
            0, 0, 0, 0), "JFormDesigner Evaluation",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog",
                java.awt.Font.BOLD, 12), java.awt.Color.red), getBorder()));
    addPropertyChangeListener(new java.beans.PropertyChangeListener() {
      public void propertyChange(java.beans.PropertyChangeEvent e) {
        if ("border".equals(e.getPropertyName()))
          throw new RuntimeException();
      }
    });

    setLayout(new FormLayout("default, $lcgap, 107dlu",
        "2*(default, $lgap), default"));

    // ---- button1 ----
    button1.setText("text");
    button1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        button1ActionPerformed(e);
      }
    });
    add(button1, cc.xy(3, 5));
    // JFormDesigner - End of component initialization //GEN-END:initComponents
  }
}
