package ru.ipo.dces.client;

import info.clearthought.layout.TableLayout;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

import ru.ipo.dces.clientservercommunication.ContestDescription;

/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 11.04.2009
 * Time: 18:59:58
 */
public class ContestChoosingPanel extends JPanel {

  private ContestDescription contest = null;

  private JLabel selectedContestLabel;
  private JButton selectContestButton;

  private ChooseContestDialog chooseContestDialog = new ChooseContestDialog(Controller.getClientDialog());
    
  public ContestChoosingPanel() {
    setLayout(new TableLayout(new double[][]{
            {20, 60, TableLayout.FILL, 80}, {TableLayout.FILL}
    }));

    selectedContestLabel = new JLabel();
    selectedContestLabel.setFont(selectedContestLabel.getFont().deriveFont(Font.BOLD));
    selectContestButton = new JButton("Выбрать");

    add(new JLabel("Контест:"), "1, 0");
    add(selectedContestLabel, "2, 0");
    add(selectContestButton, "3, 0");

    selectContestButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ContestDescription contest = chooseContestDialog.run();
        if (contest != null) {
          setContest(contest);
          Controller.contestSelected(contest);
        }
      }
    });
  }

  public void setChooserVisible(boolean visible) {
    selectContestButton.setVisible(visible);
  }

  public ContestDescription getContest() {
    return contest;
  }

  public void setContest(ContestDescription contest) {    
    this.contest = contest;
    String contestDisplayName = contest == null ? "" : contest.name;
    selectedContestLabel.setText(contestDisplayName);
  }
}
