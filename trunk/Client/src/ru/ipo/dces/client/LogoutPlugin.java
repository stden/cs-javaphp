package ru.ipo.dces.client;

import java.awt.event.*;

import javax.swing.*;

import ru.ipo.dces.pluginapi.*;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.*;

/** ����������� �� �������� */
public class LogoutPlugin extends Plugin {

  public LogoutPlugin(PluginEnvironment env, final ClientDialog clientDialog) {
    super(env);

    setLayout(new FormLayout(new ColumnSpec[] { FormFactory.DEFAULT_COLSPEC,
        FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("49dlu:grow(2.0)"),
        FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC },
        new RowSpec[] { FormFactory.DEFAULT_ROWSPEC,
            FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
            FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
            FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default") }));

    env.setTitle("Logout");

    JButton reloadButton = new JButton();
    JLabel hintLabel = new JLabel(
        "����� �� ������ ��������� ������� � ��������");
    reloadButton.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent arg0) {
        if (JOptionPane.showConfirmDialog(null, "������������� �����?",
            "�������������", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
          ClientData.sessionID = null;
        clientDialog.initialState();
      }
    });
    reloadButton.setText("�����");
    add(hintLabel, new CellConstraints(3, 1));
    add(reloadButton, new CellConstraints(5, 1));

  }

}