package ru.ipo.dces.plugins.admin.beans;

import ru.ipo.dces.clientservercommunication.*;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Ilya
 * Date: 31.07.2009
 * Time: 15:45:36
 */
public class ContestAdjustmentBean {

  private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

  private boolean modified;
  private int contestType; // -1 = no contest, 0 = new contest, 1 = existing contest
  private ContestDescriptionBean contestDescription;
  private DefaultListModel problemsListModel;
  private DefaultListSelectionModel problemsListSelectionModel;

  private final PropertyChangeListener modifiedListener = new PropertyChangeListener() {
    public void propertyChange(PropertyChangeEvent evt) {
      setModified(true);
    }
  };

  public ContestAdjustmentBean() {
    contestType = -1;
    contestDescription = new ContestDescriptionBean();
    problemsListModel = new DefaultListModel();
    problemsListSelectionModel = new DefaultListSelectionModel();

    addListeners();
  }

  public void setData(GetContestDataResponse gcdr) {
    contestDescription.setData(gcdr.contest);

    problemsListModel.clear();
    for (ProblemDescription pd : gcdr.problems) {
      ProblemDescriptionBean pdb = new ProblemDescriptionBean();
      pdb.setData(pd);
      problemsListModel.addElement(pdb);
    }
  }

  public ContestDescriptionBean getContestDescription() {
    return contestDescription;
  }

  public DefaultListModel getProblemsListModel() {
    return problemsListModel;
  }

  public DefaultListSelectionModel getProblemsListSelectionModel() {
    return problemsListSelectionModel;
  }

  /*public void setContestDescription(ContestDescriptionBean contestDescription) {
    ContestDescriptionBean oldValue = this.contestDescription;
    this.contestDescription = contestDescription;
    pcs.firePropertyChange("contestDescription", oldValue, contestDescription);
  }*/

  public int getContestType() {
    return contestType;
  }

  public void setContestType(int contestType) {
    int oldValue = this.contestType;
    this.contestType = contestType;
    pcs.firePropertyChange("contestType", oldValue, contestType);
  }

  public boolean isModified() {
    return modified;
  }

  public void setModified(boolean modified) {
    boolean oldValue = this.modified;
    this.modified = modified;
    pcs.firePropertyChange("modified", oldValue, modified);
  }

  //PropertyChangeSupport delegation

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

  //modified listeners
  private void addListeners() {
    contestDescription.addPropertyChangeListener(modifiedListener);
    contestDescription.getResultsAccessPolicy().addPropertyChangeListener(modifiedListener);
    contestDescription.getContestTiming().addPropertyChangeListener(modifiedListener);

    problemsListModel.addListDataListener(new ListDataListener() {
      public void intervalAdded(ListDataEvent e) {
        setModified(true);
      }

      public void intervalRemoved(ListDataEvent e) {
        setModified(true);
      }

      public void contentsChanged(ListDataEvent e) {
        setModified(true);
      }
    });
  }

  public ProblemDescriptionBean newProblemBean() {
    ProblemDescriptionBean bean = new ProblemDescriptionBean();
    bean.addPropertyChangeListener(modifiedListener);
    return bean;    
  }

  public ContestDescription getAdjustmentForContestDescription(ContestDescription oldData) {
    //TODO implement
    return contestDescription.getData();
  }

  public ProblemDescription[] getAdjustmentForProblemsDescription(ProblemDescription[] oldData) {
    //TODO implement
    return null;
  }
}
