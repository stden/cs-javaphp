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

  private byte[] bytes; //initial bytes
  private byte[] newBytes; //new zipped bytes
  private File file; //new file, null if nothing is to be changed

  public void setData(byte[] bytes) {
    setBytes(bytes);
    setFile(null);
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

  /*public byte[] getBytes() {
    return bytes;
  }*/

  private void setBytes(byte[] bytes) {
    byte[] oldValue = this.bytes;
    this.bytes = bytes;
    pcs.firePropertyChange("bytes", oldValue, bytes);
  }

  public File getFile() {
    return file;
  }

  public void setFile(File file) {
    File oldValue = this.file;
    try {
      vcs.fireVetoableChange("file", oldValue, file);
    } catch (PropertyVetoException e) {
      return; //nothing changed, don't do anything
    }
    this.file = file;
    pcs.firePropertyChange("file", oldValue, file);
  }

  /*public byte[] getNewBytes() {
    return newBytes;
  }*/

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
