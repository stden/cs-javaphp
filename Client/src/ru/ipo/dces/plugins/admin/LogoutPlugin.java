package ru.ipo.dces.plugins.admin;

import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.pluginapi.PluginEnvironment;
import ru.ipo.dces.client.Controller;
import ru.ipo.dces.clientservercommunication.*;

import javax.swing.*;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: ����
 * Date: 11.12.2008
 * Time: 20:06:45
 */
public class LogoutPlugin extends JPanel implements Plugin {
  private JButton logoutButton;
  private JPanel mainPanel;
  private JButton refreshProblemsButton;
  private JButton refreshPluginsButton;

  /**
   * ������������� plugin'�
   *
   * @param env environment for the plugin
   */
  public LogoutPlugin(PluginEnvironment env) {
    $$$setupUI$$$();
    env.setTitle("- ���������� -");

    logoutButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (JOptionPane.showConfirmDialog(null, "������������� �����?",
                "�������������", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
          Controller.logout();
      }
    });
    refreshProblemsButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (Controller.getUserType() != UserDescription.UserType.Participant) {
          JOptionPane.showMessageDialog(null, "������ ��������� ����� ��������� ������ �����");
          return;
        }

        try {
          Controller.getClientDialog().clearLeftPanel();
          //the first plugin will be selected after this call
          Controller.refreshParticipantInfo(true, false);
          JOptionPane.showMessageDialog(null, "������� ������� ���������");
        } catch (Exception ee) {
          JOptionPane.showMessageDialog(null, "�� ������� �������� �������");
          Controller.getClientDialog().initialState();
        }
      }
    });
    refreshPluginsButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (Controller.getUserType() == UserDescription.UserType.SuperAdmin) {
          JOptionPane.showMessageDialog(null, "����� ������������� �� ����� �������� ������");
          return;
        }

        try {
          Controller.getClientDialog().clearLeftPanel();
          //the first plugin will be selected after this call
          Controller.refreshParticipantInfo(false, true); //refresh = false
          JOptionPane.showMessageDialog(null, "������ ����� ������� ���������");
        } catch (Exception ee) {
          JOptionPane.showMessageDialog(null, "�� ������� �������� ������ �����");
          Controller.getClientDialog().initialState();
        }
      }
    });
  }

  private void createUIComponents() {
    mainPanel = this;
  }

  public JPanel getPanel() {
    return this;
  }

  public void activate() {
    //do nothing
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
    mainPanel.setLayout(new FormLayout("fill:max(d;4px):grow,left:4dlu:noGrow,fill:160dlu:noGrow,left:4dlu:noGrow,fill:80dlu:noGrow,left:4dlu:noGrow,fill:max(d;4px):grow", "center:12dlu:noGrow,top:4dlu:noGrow,center:20dlu:noGrow,top:8dlu:noGrow,center:20dlu:noGrow,top:8dlu:noGrow,center:8dlu:noGrow,top:4dlu:noGrow,center:8dlu:noGrow,top:8dlu:noGrow,center:20dlu:noGrow,top:4dlu:noGrow,center:10dlu:noGrow"));
    final JLabel label1 = new JLabel();
    label1.setText("���������� ������� � ��������");
    CellConstraints cc = new CellConstraints();
    mainPanel.add(label1, cc.xy(3, 11));
    refreshPluginsButton = new JButton();
    refreshPluginsButton.setText("��������");
    mainPanel.add(refreshPluginsButton, cc.xy(5, 5, CellConstraints.DEFAULT, CellConstraints.CENTER));
    refreshProblemsButton = new JButton();
    refreshProblemsButton.setText("��������");
    mainPanel.add(refreshProblemsButton, cc.xy(5, 3));
    final JLabel label2 = new JLabel();
    label2.setText("�������� ������ �����");
    mainPanel.add(label2, cc.xy(3, 5));
    final JLabel label3 = new JLabel();
    label3.setText("�������� ������� ������");
    mainPanel.add(label3, cc.xy(3, 3));
    final JSeparator separator1 = new JSeparator();
    mainPanel.add(separator1, cc.xyw(3, 8, 3, CellConstraints.FILL, CellConstraints.FILL));
    logoutButton = new JButton();
    logoutButton.setText("�����");
    mainPanel.add(logoutButton, cc.xy(5, 11));
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return mainPanel;
  }
}
