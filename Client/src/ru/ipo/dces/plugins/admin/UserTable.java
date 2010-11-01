package ru.ipo.dces.plugins.admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class UserTable extends JTable {
    private UserTableModel userDataTableModel = new UserTableModel();
    protected final String[] COLUMN_NAMES = new String [] {"Поле", "Значение"};

  //TODO add parameter for _selected_ values
    public void setKeys(String[] keys) {

        userDataTableModel.setRowCount(keys.length);

        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            userDataTableModel.setValueAt(key, i, 0);
            userDataTableModel.setValueAt("", i, 1);
        }
    }

    public void setValues(String[] values) {

        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            userDataTableModel.setValueAt(value, i, 1);
        }
    }

    public String[] getValues()
    {
        String[] v = new String[getRowCount()];
        for (int i = 0; i < v.length; i++)
            v[i] = getValue(i);

        return v;
    }

    public class UserTableModel extends DefaultTableModel {
        public boolean isCellEditable(int row, int column) {
            return column == 1;
        }
    }

    public UserTable() {
        super();    //To change body of overridden methods use File | Settings | File Templates.

        this.setModel(userDataTableModel);
        this.getTableHeader().setReorderingAllowed(false);
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setCellSelectionEnabled(true);

        userDataTableModel.addColumn(COLUMN_NAMES[0], new String[]{});
        userDataTableModel.addColumn(COLUMN_NAMES[1], new String[]{});

        this.getColumnModel().getColumn(0).setCellRenderer(new TableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value.toString());
                label.setFont(label.getFont().deriveFont(Font.BOLD));
                return label;
            }
        });

        this.putClientProperty("JTable.autoStartsEdit", Boolean.TRUE);
        this.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    }

    //TODO add parameter for _selected_ values
    public void setData(String[] key, String[] value)
    {
        if(key.length != value.length) throw new IllegalArgumentException();

        String[][] a = new String[key.length][2];

        for (int i = 0; i < a.length; i++)
            a[i] = new String[]{key[i], value[i]};

        userDataTableModel.setDataVector(a, COLUMN_NAMES);
    }

    public String getKey(int row)
    {
        return (String) userDataTableModel.getValueAt(row, 0);
    }

    public String getValue(int row)
    {
        return (String) userDataTableModel.getValueAt(row, 1);
    }

    public int getRowCount()
    {
        return userDataTableModel.getRowCount();
    }
}
