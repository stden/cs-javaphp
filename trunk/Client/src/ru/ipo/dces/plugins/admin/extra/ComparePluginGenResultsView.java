package ru.ipo.dces.plugins.admin.extra;

import info.clearthought.layout.TableLayout;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Ilya
 * Date: 22.12.2009
 * Time: 16:10:55
 */
public class ComparePluginGenResultsView extends JFrame {
  private String[] headers;
  private String[][] minorHeaders;

  private HashMap<String, String[][]> el2ind = new HashMap<String, String[][]>();
  private JList list;
  private JTextPane text;

  public ComparePluginGenResultsView(final String[] headers, final String[][] minorHeaders, String[][][] table, int selInd) {
    this.headers = headers;
    this.minorHeaders = minorHeaders;

    setInterface();

    String[] participants = new String[table.length];

    int ind = 0;
    for (String[][] p : table) {
      int res1 = 0;
      int res2 = 0;

      for (int i = 2; i < p.length; i++) {
        String[] probData = p[i];
        if (probData[2].equals("+")) {
          if (probData[3].equals("1"))
            res1++;
          else
            res2++;
        }
      }

      String ps = "id" + p[0][0] + ": " + p[0][1] + " = " + res1 + "|" + res2;
      el2ind.put(ps, p);
      participants[ind++] = ps;
    }

    list.setListData(participants);
    list.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        String value = (String) list.getSelectedValue();
        if (value == null) {
          text.setText("");
          return;
        }

        String[][] pdata = el2ind.get(value);

        StringBuilder txt = new StringBuilder("<body BGCOLOR=\"#EEEEEE\"><center><b>Результаты</b></center><br/>");
        for (int i = 2; i < headers.length; i++) {
          txt.append("<b>&nbsp;").append(headers[i]).append("</b><br/>");
          txt.append("<table border=\"0\">");
          for (int j = 0; j < minorHeaders[i].length; j++)
            txt.append("<tr><td width=\"40\"></td><td>")
                    .append(minorHeaders[i][j]).
                    append("</td><td>").
                    append(pdata[i][j]).
                    append("</td></tr>");
          txt.append("</table>");
        }

        //ComparePluginGenResultsView.this.pack();
        text.setText(txt.toString());
      }
    });

    if (selInd != -1)
      list.setSelectedIndex(selInd);
  }

  private void setInterface() {
    setLayout(new TableLayout(new double[]{200, TableLayout.FILL},
            new double[]{TableLayout.FILL}));

    list = new JList();
    text = new JTextPane();
    text.setContentType("text/html");

    list.setBorder(new LineBorder(Color.black));
    text.setBorder(new LineBorder(Color.black));
    text.setEditable(false);

    add(list, "0 0");
    add(new JScrollPane(text), "1 0");

    setSize(640,480);
    setLocationRelativeTo(null);
    setVisible(true);
  }

}