package ru.ipo.dces.plugins;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;
import ru.ipo.dces.pluginapi.DCESPluginLoadable;
import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.pluginapi.PluginEnvironment;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.File;
import java.io.StringReader;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Илья
 * Date: 07.12.2008
 * Time: 20:08:15
 */
@DCESPluginLoadable
public class Main extends JPanel implements Plugin {
  private JTextPane statementTextPane;
  private JPanel pluginPanel;
  private JButton submitButton;
  private JTextField answerTextField;

  private final PluginEnvironment env;

  public Main(PluginEnvironment pluginEnvironment) {
    env = pluginEnvironment;
    env.setTitle(env.getProblemName());

    $$$setupUI$$$();
    addListeners();

    showStatement();
  }

  private int getPriority(String extension, String filename) {
    if (extension.equals("html"))
      if (filename.equals("statement.html"))
        return 5;
      else
        return 4;
    else if (extension.equals("rtf"))
      return 3;
    else if (extension.equals("txt"))
      return 2;
    else if (extension.equals("bmp") || extension.equals("gif") || extension.equals("jpeg") || extension.equals("jpg") || extension.equals("png"))
      return 1;
    else
      return 0;

  }

  private void showStatement() {

    /*TODO
    * 1. get directory env.getProblemFolder()
    * 2. if there is statement.html -> show it
    * 3. else if there is a set of html files -> show any
    * 4. else if there is no html, then picture (.gif .jpeg .jpg .png)
    * 5. else if there is a .txt .rtf -> show
    * 6. (html <- rtf <- txt <- picture (gif <- jpeg <- png) - priorities
    */

    File folder = env.getProblemFolder();

    if (!folder.isDirectory()) {
      return;
    }

    final String title = "Заголовок";
    String resName = "";
    String resExt = "";
    int curPriority = 0;
    String filename = "";

    try {
      for (File f : folder.listFiles()) {
        if (f.isFile()) {
          filename = f.getName();
          String[] fileParts = filename.split("\\.");
          String ext = fileParts[fileParts.length - 1];

          switch (getPriority(ext.toLowerCase(), filename.toLowerCase())) {
            case 5:
              curPriority = 5;
              resExt = "text/html";
              resName = "file:///" + folder.getCanonicalPath() + '/' + filename;
              break;

            case 4:
              if (curPriority < 4) {
                curPriority = 4;
                resExt = "text/html";
                resName = "file:///" + folder.getCanonicalPath() + '/' + filename;
              }
              break;
            case 3:
              if (curPriority < 3) {
                curPriority = 3;
                resExt = "text/rtf";
                resName = "file:///" + folder.getCanonicalPath() + '/' + filename;
              }
              break;
            case 2:
              if (curPriority < 2) {
                curPriority = 2;
                resExt = "text/plain";
                resName = "file:///" + folder.getCanonicalPath() + '/' + filename;
              }
              break;
            case 1:
              if (curPriority < 1) {
                curPriority = 1;
                resExt = "text/html";
              }

              break;
            case 0:
              break;
          }
        }
      }

      if (curPriority == 1) {
        statementTextPane.read(new StringReader(
                "<html>" +
                        "<head>" +
                        "<meta http-equiv=\"Content-Type\" content=\"text/html\">" +
                        "<title>" + title + "</title>" +
                        "</head>" +
                        "<body>" +
                        "<base href=\"" + "file:///" + folder.getCanonicalPath() + '/' + "\"/>" +
                        "<img width=100% height=100% src=\"" + filename.toLowerCase() + "\" alt=\"Условие задачи\" />" +
                        "</body>" +
                        "</html>"), null);
      } else if (curPriority != 0) {
        statementTextPane.setContentType(resExt);
        statementTextPane.setPage(resName);
      } else
        throw new IOException();
    }
    catch (IOException e) {
      Document d = statementTextPane.getDocument();
      try {
        d.remove(0, d.getLength());
        d.insertString(0, "Не удается отобразить условие", null);
      } catch (BadLocationException e1) {
        //do nothing
      }
    }

    /*   statementTextPane.setContentType("text/html");
    try {
        statementTextPane.setPage("file:///" + env.getProblemFolder().getCanonicalPath() + '/' + "statement.html");
    } catch (IOException e) {
        Document d = statementTextPane.getDocument();
        try {
            d.remove(0, d.getLength());
            d.insertString(0, "Не удается отобразить условие", null);
        } catch (BadLocationException e1) {
            //do nothing
        }
    }*/
  }

  private void addListeners() {
    submitButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        /**TODO
         * add a task solving status to this component:
         * - If the task is solved, prohibit to send a solution to the server!!!
         * - If not solved then permit sending a solution
         *
         * Represent a status (see SamplePluginProtocol.txt) with a label on the form (= status of a problem being solved)
         * Statuses: not sent (can send), sent but wrong (can send), correct (can't send)
         * If 'solved' from a server then green color and "Задача решена" message else red and other message
         */

        if (answerTextField.getText().length() == 0) {
          JOptionPane.showMessageDialog(null, "Вы не ввели ответ");
          return;
        }
        if (JOptionPane.showConfirmDialog(null, "Подтвердите отсылку решения") == JOptionPane.YES_OPTION) {
          HashMap<String, String> res = new HashMap<String, String>();
          res.put("answer", answerTextField.getText());
          try {
            final HashMap<String, String> ans = env.submitSolution(res);
            if (ans.get("result").equals("answer accepted"))
              JOptionPane.showMessageDialog(null, "Вы дали правильный ответ!");
            else if (ans.get("result").equals("wrong answer"))
              JOptionPane.showMessageDialog(null, "Вы дали неправильный ответ!!");
            else if (ans.get("result").equals("no submissions left"))
              JOptionPane.showMessageDialog(null, "У Вас более не осталось попыток");
            else
              JOptionPane.showMessageDialog(null, "Ответ сервера: " + ans.get("result"));
          }
          catch (GeneralRequestFailureException e1) {
            JOptionPane.showMessageDialog(null, "Не удалось связаться с сервером. Ошибка: " + e1.getMessage());
          }
        }
      }
    });
  }

  private void createUIComponents() {
    pluginPanel = this;
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
    pluginPanel.setLayout(new FormLayout("fill:0dlu:noGrow,left:4dlu:noGrow,fill:40dlu:noGrow,left:4dlu:noGrow,fill:140dlu:grow,left:4dlu:noGrow,fill:60dlu:noGrow,left:4dlu:noGrow,fill:0dlu:noGrow", "center:max(d;0px):noGrow,top:4dlu:noGrow,center:12dlu:noGrow,top:4dlu:noGrow,center:80dlu:grow,top:4dlu:noGrow,center:20dlu:noGrow,top:4dlu:noGrow,center:20dlu:noGrow"));
    submitButton = new JButton();
    submitButton.setText("Ответить");
    CellConstraints cc = new CellConstraints();
    pluginPanel.add(submitButton, cc.xy(7, 7));
    answerTextField = new JTextField();
    pluginPanel.add(answerTextField, cc.xy(5, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
    final JLabel label1 = new JLabel();
    label1.setText("Условие задачи");
    pluginPanel.add(label1, cc.xyw(3, 3, 5));
    final JLabel label2 = new JLabel();
    label2.setText("Ответ");
    pluginPanel.add(label2, cc.xy(3, 7));
    final JScrollPane scrollPane1 = new JScrollPane();
    pluginPanel.add(scrollPane1, cc.xyw(3, 5, 5, CellConstraints.FILL, CellConstraints.FILL));
    statementTextPane = new JTextPane();
    statementTextPane.setEditable(false);
    scrollPane1.setViewportView(statementTextPane);
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return pluginPanel;
  }
}
