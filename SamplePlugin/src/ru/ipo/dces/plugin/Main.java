package ru.ipo.dces.plugin;

import java.awt.event.*;

import javax.swing.*;

import ru.ipo.dces.pluginapi.*;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.*;

public class Main extends Plugin {

  private static final long serialVersionUID = -8533952562620075503L;

  public Main(final PluginEnvironment client) {
    super(client);

    client.setTitle("Тестовый Plugin");

    setLayout(new FormLayout(new ColumnSpec[] { FormFactory.DEFAULT_COLSPEC,
        FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("49dlu:grow(2.0)"),
        FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC },
        new RowSpec[] { FormFactory.DEFAULT_ROWSPEC,
            FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
            FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
            FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default") }));

    final JLabel statementLabel = new JLabel();
    statementLabel.setText("Условие задачи");
    add(statementLabel, new CellConstraints(1, 1));

    final JTextField answer = new JTextField();
    add(answer, new CellConstraints(1, 3));

    JButton submit = new JButton();
    submit.setText("Проверить");
    submit.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        client.submitSolution(new Answer(answer.getText()));
      }

    });
    add(submit, new CellConstraints(3, 3));
  }
}
