package ru.ipo.dces.plugins.admin.beans;

import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.utils.ZipUtils;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;
import java.beans.*;
import java.io.File;
import java.io.IOException;

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

  private GetContestDataResponse freshContestData;

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
    freshContestData = gcdr;

    contestDescription.setData(gcdr.contest);

    problemsListModel.clear();
    for (ProblemDescription pd : gcdr.problems) {
      ProblemDescriptionBean pdb = new ProblemDescriptionBean();
      pdb.setData(pd);
      problemsListModel.addElement(pdb);
    }
  }

  public void setDefault() {
    contestDescription.setDefault();
    problemsListModel.clear();
  }

  public void undoChanges() {
    if (freshContestData == null)
      setDefault();
    else
      setData(freshContestData);

    setModified(false);
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

  public GetContestDataResponse getFreshContestData() {
    return freshContestData;
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

    final ZipBean statementZipBean = bean.getAnswerData();
    final ZipBean answerZipBean = bean.getStatementData();

    statementZipBean.addVetoableChangeListener("file", new ZipListener(statementZipBean));
    answerZipBean.addVetoableChangeListener("file", new ZipListener(answerZipBean));

    statementZipBean.addPropertyChangeListener(modifiedListener);
    answerZipBean.addPropertyChangeListener(modifiedListener);

    bean.setDefault();
    return bean;
  }

  @SuppressWarnings({"UnusedDeclaration"})
  public ContestDescription getAdjustmentForContestDescription(ContestDescription oldData) {
    //TODO think of better implementation
    return contestDescription.getData();
  }

  public ProblemDescription[] getAdjustmentForProblemsDescription(ProblemDescription[] oldProblems) {
    final int newProblemsCount = problemsListModel.getSize();

    ProblemDescription[] resultProblems = new ProblemDescription[newProblemsCount];
    boolean areEqual = oldProblems.length == newProblemsCount; //means nothing changed at all

    //main loop to fill results array
    for (int i = 0; i < newProblemsCount; i++) {

      ProblemDescription newP = ((ProblemDescriptionBean) problemsListModel.get(i)).getData();
      ProblemDescription resP = new ProblemDescription();

      if (newP.id != -1) {
        ProblemDescription oldP = null;
        for (int j = 0; j < oldProblems.length; j++) {
          ProblemDescription problem = oldProblems[j];
          if (problem.id == newP.id) {
            areEqual &= i == j;
            oldP = problem;
            break;
          }
        }

        if (oldP == null)
          throw new IndexOutOfBoundsException("Didn't found appropriate problem in this.problems array");

        resP.id = oldP.id;

        if (!oldP.name.equals(newP.name)) {
          resP.name = newP.name;
          areEqual = false;
        } else resP.name = null;

        resP.answerData = newP.answerData;
        if (newP.answerData != null) areEqual = false;
        resP.statementData = newP.statementData;
        if (newP.statementData != null) areEqual = false;

        if (!oldP.serverPluginAlias.equals(newP.serverPluginAlias)) {
          resP.serverPluginAlias = newP.serverPluginAlias;
          areEqual = false;
        } else resP.serverPluginAlias = null;

        if (!oldP.clientPluginAlias.equals(newP.clientPluginAlias)) {
          resP.clientPluginAlias = newP.clientPluginAlias;
          areEqual = false;
        } else resP.clientPluginAlias = null;
      } else { //if new problem was added
        areEqual = false;
        resP.id = -1;
        resP.name = newP.name;
        resP.answerData = newP.answerData;
        resP.statementData = newP.statementData;
        resP.serverPluginAlias = newP.serverPluginAlias;
        resP.clientPluginAlias = newP.clientPluginAlias;
      }

      resultProblems[i] = resP;
    }

    if (areEqual)
      return null;

    return resultProblems;
  }

  private class ZipListener implements VetoableChangeListener {
    private ZipBean zipBean;

    public ZipListener(ZipBean zipBean) {
      this.zipBean = zipBean;
    }

    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
      File f = (File) evt.getNewValue();

      if (f == null) return;

      try {
        final byte[] bytes = ZipUtils.zip(f);
        zipBean.setNewBytes(bytes);
      } catch (IOException e) {
        throw new PropertyVetoException("Failed to zip the file", evt);
      }
    }
  }
}
