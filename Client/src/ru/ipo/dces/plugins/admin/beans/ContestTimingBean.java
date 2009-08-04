package ru.ipo.dces.plugins.admin.beans;

import ru.ipo.dces.clientservercommunication.ContestTiming;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

/**
 * Created by IntelliJ IDEA.
 * User: Ilya
 * Date: 03.08.2009
 * Time: 19:39:05
 */
public class ContestTimingBean {

  private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

  private boolean selfContestStart;
  private int maxContestDuration;
  private int contestEndingStart;
  private int contestEndingFinish;

  public void setData(ContestTiming ct) {
    setSelfContestStart(ct.selfContestStart);
    setMaxContestDuration(ct.maxContestDuration);
    setContestEndingStart(ct.contestEndingStart);
    setContestEndingFinish(ct.contestEndingFinish);
  }

  public ContestTiming getData() {
    ContestTiming ct = new ContestTiming();

    ct.selfContestStart = this.selfContestStart;
    ct.maxContestDuration = this.maxContestDuration;
    ct.contestEndingStart = this.contestEndingStart;
    ct.contestEndingFinish = this.contestEndingFinish;

    return ct;
  }

  public ContestTimingBean() {
  }

  public boolean isSelfContestStart() {
    return selfContestStart;
  }

  public void setSelfContestStart(boolean selfContestStart) {
    boolean oldValue = this.selfContestStart;
    this.selfContestStart = selfContestStart;
    pcs.firePropertyChange("selfContestStart", oldValue, selfContestStart);
  }

  public int getMaxContestDuration() {
    return maxContestDuration;
  }

  public void setMaxContestDuration(int maxContestDuration) {
    int oldValue = this.maxContestDuration;
    this.maxContestDuration = maxContestDuration;
    pcs.firePropertyChange("maxContestDuration", oldValue, maxContestDuration);
  }

  public int getContestEndingStart() {
    return contestEndingStart;
  }

  public void setContestEndingStart(int contestEndingStart) {
    int oldValue = this.contestEndingStart;
    this.contestEndingStart = contestEndingStart;
    pcs.firePropertyChange("contestEndingStart", oldValue, contestEndingStart);
  }

  public int getContestEndingFinish() {
    return contestEndingFinish;
  }

  public void setContestEndingFinish(int contestEndingFinish) {
    int oldValue = this.contestEndingFinish;
    this.contestEndingFinish = contestEndingFinish;
    pcs.firePropertyChange("contestEndingFinish", oldValue, contestEndingFinish);
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

  @Override
  public String toString() {
    if (!selfContestStart)
      return "no self";
    else
      return "max=" + maxContestDuration +
            " s=" + contestEndingStart +
            " f=" + contestEndingFinish;
  }
}
