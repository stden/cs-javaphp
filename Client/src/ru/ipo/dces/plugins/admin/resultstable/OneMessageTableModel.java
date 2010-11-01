package ru.ipo.dces.plugins.admin.resultstable;

import javax.swing.table.TableModel;
import javax.swing.event.TableModelListener;

/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 13.04.2009
 * Time: 20:07:53
 */
public class OneMessageTableModel implements TableModel {

  private String message;

  //TODO make this message be centered

  public OneMessageTableModel(String message) {
    this.message = message;
  }

  public int getRowCount() {
    return 0;
  }

  public int getColumnCount() {
    return 1;
  }

  public String getColumnName(int columnIndex) {
    return message;
  }

  public Class<?> getColumnClass(int columnIndex) {
    return String.class;
  }

  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return false;
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    return "";
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
