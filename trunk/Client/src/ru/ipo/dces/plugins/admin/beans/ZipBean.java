package ru.ipo.dces.plugins.admin.beans;

import ru.ipo.dces.utils.ZipUtils;

import java.beans.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Ilya
 * Date: 17.08.2009
 * Time: 10:55:43
 */
public class ZipBean {

  private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
  private VetoableChangeSupport vcs = new VetoableChangeSupport(this);

  private byte[] bytes;
  private byte[] newBytes;
  private File file;

  public void setData(byte[] bytes) {
    setBytes(bytes);
    try {
      setFile(null);
    } catch (PropertyVetoException e) {
      //do nothing
    }
  }

  public void setDefault() {
    setData(null);
  }

  public byte[] getData() {
    if (file == null)
      return bytes;
    else
      return newBytes;
  }

  //getters and setters

  public byte[] getBytes() {
    return bytes;
  }

  public void setBytes(byte[] bytes) {
    byte[] oldValue = this.bytes;
    this.bytes = bytes;
    pcs.firePropertyChange("bytes", oldValue, bytes);
  }

  public File getFile() {
    return file;
  }

  public void setFile(File file) throws PropertyVetoException {
    File oldValue = this.file;
    vcs.fireVetoableChange("file", oldValue, file);
    this.file = file;
    pcs.firePropertyChange("file", oldValue, file);
  }

  public byte[] getNewBytes() {
    return newBytes;
  }

  public void setNewBytes(byte[] bytes) {
    byte[] oldValue = this.newBytes;
    this.newBytes = bytes;
    pcs.firePropertyChange("newBytes", oldValue, bytes);
  }

  //pcs delegation

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    pcs.addPropertyChangeListener(listener);
  }

  public void removePropertyChangeListener(PropertyChangeListener listener) {
    pcs.removePropertyChangeListener(listener);
  }

  public PropertyChangeListener[] getPropertyChangeListeners() {
    return pcs.getPropertyChangeListeners();
  }

  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    pcs.addPropertyChangeListener(propertyName, listener);
  }

  public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    pcs.removePropertyChangeListener(propertyName, listener);
  }

  public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
    return pcs.getPropertyChangeListeners(propertyName);
  }

  //vcs delegation

  public void addVetoableChangeListener(VetoableChangeListener listener) {
    vcs.addVetoableChangeListener(listener);
  }

  public void removeVetoableChangeListener(VetoableChangeListener listener) {
    vcs.removeVetoableChangeListener(listener);
  }

  public VetoableChangeListener[] getVetoableChangeListeners() {
    return vcs.getVetoableChangeListeners();
  }

  public void addVetoableChangeListener(String propertyName, VetoableChangeListener listener) {
    vcs.addVetoableChangeListener(propertyName, listener);
  }

  public void removeVetoableChangeListener(String propertyName, VetoableChangeListener listener) {
    vcs.removeVetoableChangeListener(propertyName, listener);
  }

  public VetoableChangeListener[] getVetoableChangeListeners(String propertyName) {
    return vcs.getVetoableChangeListeners(propertyName);
  }
}
