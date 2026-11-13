package ru.ipo.dces.plugins.admin.beans;

import ru.ipo.dces.clientservercommunication.ProblemDescription;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

/**
 * Created by IntelliJ IDEA.
 * User: Ilya
 * Date: 03.08.2009
 * Time: 19:49:02
 */
public class ProblemDescriptionBean {

  private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

  private int id;
  private String clientPluginAlias;
  private String serverPluginAlias;
  private String name;
  private ZipBean statementData;
  private ZipBean answerData;

  ProblemDescriptionBean() {
    statementData = new ZipBean();
    answerData = new ZipBean();
  }

  public void setData(ProblemDescription pd) {
    setId(pd.id);
    setClientPluginAlias(pd.clientPluginAlias);
    setServerPluginAlias(pd.serverPluginAlias);
    setName(pd.name);
    statementData.setData(pd.statementData);
    answerData.setData(pd.answerData);    
  }

  public void setDefault () {
    setId(-1);
    setClientPluginAlias(null);
    setServerPluginAlias(null);
    setName("Новая задача");
    statementData.setDefault();
    answerData.setDefault();
  }

  public ProblemDescription getData() {
    ProblemDescription pd = new ProblemDescription();

    pd.id = this.id;
    pd.clientPluginAlias = this.clientPluginAlias;
    pd.serverPluginAlias = this.serverPluginAlias;
    pd.name = this.name;
    pd.statementData = this.statementData.getData();
    pd.answerData = this.answerData.getData();

    return pd;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getClientPluginAlias() {
    return clientPluginAlias;
  }

  public void setClientPluginAlias(String clientPluginAlias) {
    String oldValue = this.clientPluginAlias;
    this.clientPluginAlias = clientPluginAlias;
    pcs.firePropertyChange("pluginAlias", oldValue, clientPluginAlias);
  }

  public String getServerPluginAlias() {
    return serverPluginAlias;
  }

  public void setServerPluginAlias(String serverPluginAlias) {
    String oldValue = this.serverPluginAlias;
    this.serverPluginAlias = serverPluginAlias;
    pcs.firePropertyChange("serverPluginAlias", oldValue, serverPluginAlias);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    String oldValue = this.name;
    this.name = name;
    pcs.firePropertyChange("name", oldValue, name);
  }

  public ZipBean getStatementData() {
    return statementData;
  }

  public void setStatementData(ZipBean statementData) {
    ZipBean oldValue = this.statementData;
    this.statementData = statementData;
    pcs.firePropertyChange("statementData", oldValue, statementData);
  }

  public ZipBean getAnswerData() {
    return answerData;
  }

  public void setAnswerData(ZipBean answerData) {
    ZipBean oldValue = this.answerData;
    this.answerData = answerData;
    pcs.firePropertyChange("answerData", oldValue, answerData);
  }

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

  //view
  //TODO think of renderer instead
  @Override
  public String toString() {
    return name;
  }
}
