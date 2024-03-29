package ru.ipo.dces.plugins.admin;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import ru.ipo.dces.client.Controller;
import ru.ipo.dces.client.Localization;
import ru.ipo.dces.clientservercommunication.ContestDescription;
import ru.ipo.dces.clientservercommunication.UserDescription;
import ru.ipo.dces.pluginapi.PluginEnvironment;
import ru.ipo.dces.pluginapi.Plugin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: Илья
 * Date: 11.12.2008
 * Time: 20:06:45
 */
public class LogoutPlugin extends JPanel implements Plugin {

    private JButton logoutButton;
    private JPanel mainPanel;
    private JButton refreshProblemsButton;
    private JButton refreshPluginsButton;
    private JSeparator stopContestSeparator;
    private JLabel stopContestLabel;
    private JButton stopContestButton;

    /**
     * Инициализация plugin'а
     *
     * @param env environment for the plugin
     */
    public LogoutPlugin(PluginEnvironment env) {
        $$$setupUI$$$();
        env.setTitle(Localization.getAdminPluginName(LogoutPlugin.class));

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(null, "Действительно выйти?",
                        "Подтверждение", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                    Controller.logout();
            }
        });
        refreshProblemsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Controller.getContestConnection().getUser().userType != UserDescription.UserType.Participant) {
                    JOptionPane.showMessageDialog(null, "Только участники могут обновлять список задач");
                    return;
                }

                try {
                    Controller.getClientDialog().clearLeftPanel();
                    //the first plugin will be selected after this call
                    Controller.refreshParticipantInfo(true, false);
                    JOptionPane.showMessageDialog(null, "Условия успешно обновлены");
                } catch (Exception ee) {
                    JOptionPane.showMessageDialog(null, "Не удалось обновить условия");
                    Controller.getClientDialog().initialState();
                }
            }
        });
        refreshPluginsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Controller.getContestConnection().getUser().userType == UserDescription.UserType.SuperAdmin) {
                    JOptionPane.showMessageDialog(null, "Супер администратор не может обновить модули");
                    return;
                }

                try {
                    Controller.getClientDialog().clearLeftPanel();
                    //the first plugin will be selected after this call
                    Controller.refreshParticipantInfo(false, true); //refresh = false
                    JOptionPane.showMessageDialog(null, "Модули задач успешно обновлены");
                } catch (Exception ee) {
                    JOptionPane.showMessageDialog(null, "Не удалось обновить модули задач");
                    Controller.getClientDialog().initialState();
                }
            }
        });
        stopContestButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(null, "Действительно выйти?", "Подтверждение",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                    Controller.stopContest();
            }
        });
    }

    private void setStopContestControlsVisible(boolean visible) {
        stopContestSeparator.setVisible(visible);
        stopContestLabel.setVisible(visible);
        stopContestButton.setVisible(visible);
    }

    private void createUIComponents() {
        mainPanel = this;
    }

    public JPanel getPanel() {
        return this;
    }

    public void activate() {
        ContestDescription cd = Controller.getContestConnection().getContest();
        boolean visible = cd != null && cd.contestTiming != null && cd.contestTiming.selfContestStart;
        setStopContestControlsVisible(visible);
    }

    public void deactivate() {
        //do nothing
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        mainPanel.setLayout(new FormLayout("fill:max(d;4px):grow,left:4dlu:noGrow,fill:160dlu:noGrow,left:4dlu:noGrow,fill:80dlu:noGrow,left:4dlu:noGrow,fill:max(d;4px):grow", "center:12dlu:noGrow,top:4dlu:noGrow,center:20dlu:noGrow,top:8dlu:noGrow,center:20dlu:noGrow,top:8dlu:noGrow,center:8dlu:noGrow,top:4dlu:noGrow,center:8dlu:noGrow,top:8dlu:noGrow,center:20dlu:noGrow,top:4dlu:noGrow,center:20dlu:noGrow,top:4dlu:noGrow,center:20dlu:noGrow,top:4dlu:noGrow,center:10dlu:noGrow"));
        final JLabel label1 = new JLabel();
        label1.setText("Перейти к другому соревнованию");
        CellConstraints cc = new CellConstraints();
        mainPanel.add(label1, cc.xy(3, 11));
        refreshPluginsButton = new JButton();
        refreshPluginsButton.setText("Обновить");
        mainPanel.add(refreshPluginsButton, cc.xy(5, 5, CellConstraints.DEFAULT, CellConstraints.CENTER));
        refreshProblemsButton = new JButton();
        refreshProblemsButton.setText("Обновить");
        mainPanel.add(refreshProblemsButton, cc.xy(5, 3));
        final JLabel label2 = new JLabel();
        label2.setText("Обновить модули задач");
        mainPanel.add(label2, cc.xy(3, 5));
        final JLabel label3 = new JLabel();
        label3.setText("Обновить условия задачи");
        mainPanel.add(label3, cc.xy(3, 3));
        final JSeparator separator1 = new JSeparator();
        mainPanel.add(separator1, cc.xyw(3, 8, 3, CellConstraints.FILL, CellConstraints.FILL));
        logoutButton = new JButton();
        logoutButton.setText("Выход");
        mainPanel.add(logoutButton, cc.xy(5, 11));
        stopContestLabel = new JLabel();
        stopContestLabel.setText("Закончить соревнование досрочно");
        mainPanel.add(stopContestLabel, cc.xy(3, 15));
        stopContestButton = new JButton();
        stopContestButton.setText("Закончить");
        mainPanel.add(stopContestButton, cc.xy(5, 15));
        stopContestSeparator = new JSeparator();
        mainPanel.add(stopContestSeparator, cc.xyw(3, 13, 3, CellConstraints.FILL, CellConstraints.CENTER));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}