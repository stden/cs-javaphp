//package ru.ipo.dces.plugin;
//
//import java.awt.event.*;
//import java.util.HashMap;
//
//import javax.swing.*;
//
//import ru.ipo.dces.pluginapi.*;
//
//import com.jgoodies.forms.factories.FormFactory;
//import com.jgoodies.forms.layout.*;
//
//public class MainOld extends Plugin {
//
//  private static final long serialVersionUID = -8533952562620075503L;
//
//  public MainOld(final PluginEnvironment client) {
//    super(client);
//
//    client.setTitle("Тестовый Plugin");
//
//    setLayout(new FormLayout(new ColumnSpec[]{FormFactory.DEFAULT_COLSPEC,
//            FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("49dlu:grow(2.0)"),
//            FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC},
//            new RowSpec[]{FormFactory.DEFAULT_ROWSPEC,
//                    FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
//                    FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
//                    FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default")}));
//
//    final JLabel statementLabel = new JLabel();
//    statementLabel.setText("Условие задачи");
//    add(statementLabel, new CellConstraints(1, 1));
//
//    final JTextField answer = new JTextField();
//    add(answer, new CellConstraints(1, 3));
//
//    JButton submit = new JButton();
//    submit.setText("Проверить");
//    submit.addActionListener(new ActionListener() {
//
//      @Override
//      public void actionPerformed(ActionEvent arg0) {
//        try {
//          HashMap<String, String> send = new HashMap<String, String>();
//          send.put("answer", answer.getText());
//          final HashMap<String, String> res = client.submitSolution(send);
//          JOptionPane.showMessageDialog(null, res.get("result"));
//        } catch (Exception e) {
//          //TODO show message instead of the stack trace
//          e.printStackTrace();
//        }
//      }
//
//    });
//    add(submit, new CellConstraints(3, 3));
//  }
//}
