package ru.ipo.dces.client.components;

import java.awt.BorderLayout;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ru.ipo.dces.clientservercommunication.ContestDescription;
import ru.ipo.dces.plugins.admin.beans.ContestsListBean;
import ru.ipo.dces.client.components.ContestChoosingPanel;
import ru.ipo.dces.client.Controller;

public class ChooseContestDialog extends JDialog {

  private JList contestsList;
  private JLabel idLabel;
  private ContestDescription selectedContest;

  public ChooseContestDialog(/*JFrame frame*/) {
    super(Controller.getClientDialog());
    initGUI();
  }

  private void initGUI() {
    setModalityType(ModalityType.APPLICATION_MODAL);

    JPanel buttonsPanel = new JPanel();
    add(buttonsPanel, BorderLayout.SOUTH);

    idLabel = new JLabel();
    buttonsPanel.add(idLabel);
    idLabel.setText("");

    JButton okButton = new JButton();
    buttonsPanel.add(okButton);
    okButton.setText("Выбрать");

    JButton cancelButton = new JButton();
    buttonsPanel.add(cancelButton);
    cancelButton.setText("Отменить");

    contestsList = new JList();
    JScrollPane contestsListScroll = new JScrollPane(contestsList);
    add(contestsListScroll, BorderLayout.CENTER);

    setSize(400, 300);
    setTitle("Выберите соревнование");
        
    //add action
    okButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        ContestsListBean val = (ContestsListBean) contestsList.getSelectedValue();
        if (val == null)
          selectedContest = null;
        else
          selectedContest = val.getDescription();
        ChooseContestDialog.this.setVisible(false);
      }
    });

    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cancelClick();
      }
    });

    this.addWindowListener(new WindowAdapter() {     
      public void windowClosing(WindowEvent e) {
        cancelClick();
      }
    });

    contestsList.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        final ContestsListBean value = (ContestsListBean)contestsList.getSelectedValue();
        if (value == null) return;
        idLabel.setText("contest id = " + value.getDescription().contestID);
      }
    });
  }

  private void cancelClick() {
    selectedContest = null;
    this.setVisible(false);
  }

  public ContestDescription run() {
    ContestsListBean[] contestDescriptions = ContestChoosingPanel.getContestList();
    ListModel listModel = new DefaultComboBoxModel(contestDescriptions);
    contestsList.setModel(listModel);

    selectedContest = null;
    this.setVisible(true);
    return selectedContest;
  }

}
