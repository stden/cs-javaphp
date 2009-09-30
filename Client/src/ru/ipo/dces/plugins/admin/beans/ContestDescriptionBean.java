package ru.ipo.dces.plugins.admin.beans;

import ru.ipo.dces.clientservercommunication.ContestDescription;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ilya
 * Date: 03.08.2009
 * Time: 18:34:30
 */
public class ContestDescriptionBean {

  private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

  private int contestID;
  private String name;
  private String description;
  private DateBean start;
  private DateBean finish;
  private ContestDescription.RegistrationType registrationType;
  private ResultsAccessPolicyBean resultsAccessPolicy;
  private ContestTimingBean contestTiming;

  public void setDefault() {
    setContestID(-1);
    setName("Новое соревнование");
    setDescription("");
    setStart(new DateBean(new Date())); //now
    setFinish(new DateBean(new Date(new Date().getTime() + 1000 * 60 * 60))); // now + 1 hour
    setRegistrationType(ContestDescription.RegistrationType.ByAdmins);
    this.resultsAccessPolicy.setDefault();
    this.contestTiming.setDefault();
  }
  
  public void setData(ContestDescription cd) {
    setContestID(cd.contestID);
    setName(cd.name);
    setDescription(cd.description);
    setStart(new DateBean(cd.start));
    setFinish(new DateBean(cd.finish));
    setRegistrationType(cd.registrationType);
    this.resultsAccessPolicy.setData(cd.resultsAccessPolicy);
    this.contestTiming.setData(cd.contestTiming);
  }

  ContestDescriptionBean() {
    this.resultsAccessPolicy = new ResultsAccessPolicyBean();
    this.contestTiming = new ContestTimingBean();
  }

  public ContestDescription getData() {
    ContestDescription cd = new ContestDescription();

    cd.contestID = this.contestID;
    cd.name = this.name;
    cd.description = this.description;
    cd.start = this.start.getDate();
    cd.finish = this.finish.getDate();
    cd.registrationType = this.registrationType;
    cd.resultsAccessPolicy = this.resultsAccessPolicy.getData();
    cd.contestTiming = this.contestTiming.getData();

    return cd;
  }

  //getters and setters

  public int getContestID() {
    return contestID;
  }

  public void setContestID(int contestID) {
    int oldValue = this.contestID;
    this.contestID = contestID;
    pcs.firePropertyChange("contestID", oldValue, contestID);    
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    String oldValue = this.name;
    this.name = name;
    pcs.firePropertyChange("name", oldValue, name);
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    String oldValue = this.description;
    this.description = description;
    pcs.firePropertyChange("description", oldValue, description);
  }

  public DateBean getStart() {
    return start;
  }

  public void setStart(DateBean start) {
    DateBean oldValue = this.start;
    this.start = start;
    pcs.firePropertyChange("start", oldValue, start);
  }

  public DateBean getFinish() {
    return finish;
  }

  public void setFinish(DateBean finish) {
    DateBean oldValue = this.finish;
    this.finish = finish;
    pcs.firePropertyChange("finish", oldValue, finish);
  }

  public ContestDescription.RegistrationType getRegistrationType() {
    return registrationType;
  }

  public void setRegistrationType(ContestDescription.RegistrationType registrationType) {
    ContestDescription.RegistrationType oldValue = this.registrationType;
    this.registrationType = registrationType;
    pcs.firePropertyChange("registrationType", oldValue, registrationType);
  }

  public ResultsAccessPolicyBean getResultsAccessPolicy() {
    return resultsAccessPolicy;
  }

  public void setResultsAccessPolicy(ResultsAccessPolicyBean resultsAccessPolicy) {
    ResultsAccessPolicyBean oldValue = this.resultsAccessPolicy;
    this.resultsAccessPolicy = resultsAccessPolicy;
    pcs.firePropertyChange("resultsAccessPolicy", oldValue, resultsAccessPolicy);
  }

  public ContestTimingBean getContestTiming() {
    return contestTiming;
  }

  public void setContestTiming(ContestTimingBean contestTiming) {
    ContestTimingBean oldValue = this.contestTiming;
    this.contestTiming = contestTiming;
    pcs.firePropertyChange("contestTiming", oldValue, this.contestTiming);
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

}
