package ru.ipo.dces.client;

import java.awt.BorderLayout;
import java.awt.event.*;

import javax.swing.*;

import ru.ipo.dces.clientservercommunication.ContestDescription;
import ru.ipo.dces.plugins.admin.beans.ContestsListBean;

public class ChooseContestDialog extends JDialog {

  private JList   contestsList;
  private ContestDescription selectedContest;

  public ChooseContestDialog(JFrame frame) {
    super(frame);
    initGUI();
  }

  private void initGUI() {
    setModalityType(ModalityType.APPLICATION_MODAL);

    JPanel buttonsPanel = new JPanel();
    add(buttonsPanel, BorderLayout.SOUTH);

    JButton okButton = new JButton();
    buttonsPanel.add(okButton);
    okButton.setText("Выбрать");

    JButton cancelButton = new JButton();
    buttonsPanel.add(cancelButton);
    cancelButton.setText("Отменить");

    ContestsListBean[] contestDescriptions = getContestList();

    ListModel listModel = new DefaultComboBoxModel(contestDescriptions);
    contestsList = new JList();
    JScrollPane contestsListScroll = new JScrollPane(contestsList);
    add(contestsListScroll, BorderLayout.CENTER);
    contestsList.setModel(listModel);

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
  }

  private void cancelClick() {
    selectedContest = null;
    this.setVisible(false);
  }

  private ContestsListBean[] getContestList() {
    ContestDescription[] contestDescriptions = Controller.getAvailableContests();
    ContestsListBean[] result = new ContestsListBean[contestDescriptions.length];
    for (int i = 0; i < contestDescriptions.length; i++)
      result[i] = new ContestsListBean(contestDescriptions[i]);

    return result;
  }

  public ContestDescription run() {
    selectedContest = null;
    this.setVisible(true);
    return selectedContest;
  }

}
