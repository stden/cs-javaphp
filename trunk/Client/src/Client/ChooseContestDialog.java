package Client;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import org.jdesktop.application.Application;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class ChooseContestDialog extends javax.swing.JDialog {
  /**
   * Auto-generated main method to display this JDialog
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        JFrame frame = new JFrame();
        ChooseContestDialog inst = new ChooseContestDialog(frame);
        inst.setVisible(true);
      }
    });
  }

  private JPanel  buttonsPanel;
  private JButton CancelButton;

  private JButton OKButton;
  private JList   contestsList;

  public ChooseContestDialog(JFrame frame) {
    super(frame);
    initGUI();
  }

  private void initGUI() {
    try {
      {
        buttonsPanel = new JPanel();
        getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
        buttonsPanel.setPreferredSize(new java.awt.Dimension(392, 52));
        {
          OKButton = new JButton();
          buttonsPanel.add(OKButton);
          OKButton.setName("OKButton");
          OKButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
              System.out
                  .println("Selected: " + contestsList.getSelectedIndex());
              System.out.println("OKButton.actionPerformed, event=" + evt);
              // TODO add your code for OKButton.actionPerformed
            }
          });
        }
        {
          CancelButton = new JButton();
          buttonsPanel.add(CancelButton);
          CancelButton.setName("CancelButton");
        }
      }
      {
        List<Contest> c = new ArrayList<Contest>();
        c.add(new Contest("Test #1"));
        c.add(new Contest("Test #2"));
        ListModel jList1Model = new DefaultComboBoxModel(c.toArray());
        contestsList = new JList();
        getContentPane().add(contestsList, BorderLayout.CENTER);
        contestsList.setModel(jList1Model);
      }
      setSize(400, 300);
      setTitle("Choose contest");
      Application.getInstance().getContext().getResourceMap(getClass())
          .injectComponents(getContentPane());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void OKButtonActionPerformed(ActionEvent evt) {
    System.out.println("OKButton.actionPerformed, event=" + evt);
    // TODO add your code for OKButton.actionPerformed
  }

}
