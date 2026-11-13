package ru.ipo.dces.plugins.admin.resultstable;

import javax.swing.table.TableModel;
import javax.swing.event.TableModelListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 11.04.2009
 * Time: 16:26:12
 */
public class ResultsTableModel implements TableModel {

  /*
  private HashSet<TableModelListener> modelListeners = new HashSet<TableModelListener>();

  private void fireEvent(TableModelEvent e) {
    for (TableModelListener modelListener : modelListeners) {
      //TableModelEvent tableModelEvent = new TableModelEvent();
      modelListener.tableChanged(e);
    }
  }
  */

  private String[] headers;
  private String[][] minorHeaders;
  private String[][][] table;

  private final int cols;
  private final int rows;

  private HashMap<Integer, String> col2name = new HashMap<Integer, String>();

  private int ind1, ind2;

  private void col2inds(int col) {
    ind1 = 0;
    while (col >= minorHeaders[ind1].length) {
      col -= minorHeaders[ind1].length;
      ind1++;
    }
    ind2 = col;
  }

  public ResultsTableModel(String[] headers, String[][] minorHeaders, String[][][] table) {
    this.headers = headers;
    this.minorHeaders = minorHeaders;
    this.table = table;

    int c = 0;
    for (String[] minorHeader : minorHeaders)
      c += minorHeader.length;
    cols = c;

    rows = table.length;
  }

  public int getRowCount() {
    return rows;
  }

  public int getColumnCount() {
    return cols;
  }

  public String getColumnName(int columnIndex) {
    String res = col2name.get(columnIndex);
    if (res == null) {
      col2inds(columnIndex);
      if (minorHeaders[ind1].length == 1) {
        if (minorHeaders[ind1][ind2].equals(""))
          res = headers[ind1];
        else
          res = minorHeaders[ind1][ind2];
      } else {
        if (minorHeaders[ind1][ind2].equals(""))
          res = headers[ind1];
        else
          res = headers[ind1] + '.' + minorHeaders[ind1][ind2];
      }

      col2name.put(columnIndex, res);
    }

    return res;
  }

  public Class<?> getColumnClass(int columnIndex) {
    return String.class;
  }

  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return false;
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    col2inds(columnIndex);
    return table[rowIndex][ind1][ind2];
  }

  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    //do nothing
  }

  public void addTableModelListener(TableModelListener l) {
    //do nothing
  }

  public void removeTableModelListener(TableModelListener l) {
    //do nothing
  }
}
