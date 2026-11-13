package ru.ipo.dces.plugins.admin.components;

import ru.ipo.dces.plugins.admin.beans.DateBean;

import javax.swing.*;
import java.beans.PropertyEditor;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.awt.*;
import java.text.DateFormat;

/**
 * Created by IntelliJ IDEA.
 * User: Ilya
 * Date: 04.08.2009
 * Time: 17:19:27
 */
public class DateBeanPropertyEditor implements PropertyEditor {

  private DateBean value;
  private JLabel label = new JLabel();
  private DateFormat formatter = DateFormat.getInstance();
  private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

  public void setValue(Object value) {

    DateBean oldValue = this.value;
    this.value = (DateBean) value;

    if (value == null) {
      label.setText("NULL");
      return;
    }

    label.setText(formatter.format(this.value.getDate()));

    pcs.firePropertyChange(null, oldValue, value);
  }

  public Object getValue() {
    return value;
  }

  public boolean isPaintable() {
    return false;
  }

  public void paintValue(Graphics gfx, Rectangle box) {
    //do nothing
  }

  public String getJavaInitializationString() {
    return value == null ?
            "null" :
            "new DateBean(new Date(" + value.getDate().getTime() + "))";

  }

  public String getAsText() {
    return null;
  }

  public void setAsText(String text) throws IllegalArgumentException {
    //do nothing
  }

  public String[] getTags() {
    return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
  }

  public Component getCustomEditor() {
    return label;
  }

  public boolean supportsCustomEditor() {
    return true;
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    pcs.addPropertyChangeListener(listener);
  }

  public void removePropertyChangeListener(PropertyChangeListener listener) {
    pcs.removePropertyChangeListener(listener);
  }
}
