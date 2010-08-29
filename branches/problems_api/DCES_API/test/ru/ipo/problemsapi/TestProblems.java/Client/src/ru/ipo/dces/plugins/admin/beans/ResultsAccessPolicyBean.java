package ru.ipo.dces.plugins.admin.beans;

import ru.ipo.dces.clientservercommunication.ResultsAccessPolicy;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Ilya
 * Date: 03.08.2009
 * Time: 19:38:57
 */
public class ResultsAccessPolicyBean {

  private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

  private ResultsAccessPolicy.AccessPermission contestPermission;
  private ResultsAccessPolicy.AccessPermission contestEndingPermission;
  private ResultsAccessPolicy.AccessPermission afterContestPermission;

  private final HashMap<ResultsAccessPolicy.AccessPermission, String> ap2short;

  ResultsAccessPolicyBean() {
    ap2short = new HashMap<ResultsAccessPolicy.AccessPermission, String>();
    ap2short.put(ResultsAccessPolicy.AccessPermission.FullAccess, "F");
    ap2short.put(ResultsAccessPolicy.AccessPermission.OnlySelfResults, "S");
    ap2short.put(ResultsAccessPolicy.AccessPermission.NoAccess, "N");
  }

  public void setDefault() {
    setContestPermission(ResultsAccessPolicy.AccessPermission.NoAccess);
    setContestEndingPermission(ResultsAccessPolicy.AccessPermission.NoAccess);
    setAfterContestPermission(ResultsAccessPolicy.AccessPermission.FullAccess);
  }

  public void setData(ResultsAccessPolicy rap) {
    setContestPermission(rap.contestPermission);
    setContestEndingPermission(rap.contestEndingPermission);
    setAfterContestPermission(rap.afterContestPermission);
  }

  public ResultsAccessPolicy getData() {
    ResultsAccessPolicy rap = new ResultsAccessPolicy();

    rap.contestPermission = this.contestPermission;
    rap.contestEndingPermission = this.contestEndingPermission;
    rap.afterContestPermission = this.afterContestPermission;

    return rap;
  }

  public ResultsAccessPolicy.AccessPermission getContestPermission() {
    return contestPermission;
  }

  public void setContestPermission(ResultsAccessPolicy.AccessPermission contestPermission) {
    ResultsAccessPolicy.AccessPermission oldValue = this.contestPermission;
    this.contestPermission = contestPermission;
    pcs.firePropertyChange("contestPermission", oldValue, contestPermission);
  }

  public ResultsAccessPolicy.AccessPermission getContestEndingPermission() {
    return contestEndingPermission;
  }

  public void setContestEndingPermission(ResultsAccessPolicy.AccessPermission contestEndingPermission) {
    ResultsAccessPolicy.AccessPermission oldValue = this.contestEndingPermission;
    this.contestEndingPermission = contestEndingPermission;
    pcs.firePropertyChange("contestEndingPermission", oldValue, contestEndingPermission);
  }

  public ResultsAccessPolicy.AccessPermission getAfterContestPermission() {
    return afterContestPermission;
  }

  public void setAfterContestPermission(ResultsAccessPolicy.AccessPermission afterContestPermission) {
    ResultsAccessPolicy.AccessPermission oldValue = this.afterContestPermission;
    this.afterContestPermission = afterContestPermission;
    pcs.firePropertyChange("afterContestPermission", oldValue, afterContestPermission);
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
    return ap2short.get(contestPermission) + " " +
            ap2short.get(contestEndingPermission) + " " +
            ap2short.get(afterContestPermission);
  }

}
