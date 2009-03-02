package ru.ipo.dces.plugins.admin.beans;

import ru.ipo.dces.clientservercommunication.ContestDescription;
import ru.ipo.dces.clientservercommunication.ProblemDescription;

import java.util.Date;

public class AdjustContestsPluginBean {

    private int contestID;
    private String contestName;
    private Date beginDateTime;
    private Date endDateTime;
    private String contestDescription;
    private ProblemDescription[] problems;
    private ContestDescription.RegistrationType isByAdmin;

    private boolean updateAllowed = false;

    public boolean isUpdateAllowed() {
        return updateAllowed;
    }

    public void setUpdateAllowed(boolean updateAllowed) {
        this.updateAllowed = updateAllowed;
    }

    public int getContestID() {
        return contestID;
    }

    public void setContestID(int contestID) {
        this.contestID = contestID;
    }

    public AdjustContestsPluginBean() {
    }

    public String getContestName() {
        return contestName;
    }

    public void setContestName(final String contestName) {
        this.contestName = contestName;
    }

    public String compareContestName(String newContestName) {
        return !contestName.equals(newContestName) ? newContestName : null;
    }

    public String getContestDescription() {
        return contestDescription;
    }

    public void setContestDescription(final String contestDescription) {
        this.contestDescription = contestDescription;
    }

    public String compareContestDescriptions(String newDescr) {
        return !contestDescription.equals(newDescr)? newDescr : null;
    }

    public Date getBeginDateTime() {
        return beginDateTime;
    }

    public void setBeginDateTime(Date begin) {
        beginDateTime = begin;
    }

    public Date compareBeginDateTime(Date newBeginDateTime) {

        return !beginDateTime.equals(newBeginDateTime)? newBeginDateTime : null;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date end) {
        endDateTime = end;
    }

    public Date compareEndDateTime(Date newEndDateTime) {
        return !endDateTime.equals(endDateTime)? newEndDateTime : null;
    }

    public ContestDescription.RegistrationType getIsByAdmin() {
        return isByAdmin;
    }

    public void setIsByAdmin(ContestDescription.RegistrationType isByAdmin) {
        this.isByAdmin = isByAdmin;
    }

    public ContestDescription.RegistrationType compareIsByAdmin(ContestDescription.RegistrationType newIsByAdmin) {
        return !isByAdmin.equals(newIsByAdmin) ? newIsByAdmin : null;
    }

    public ProblemDescription[] getProblemDescriptions() {
        return problems;
    }

    public void setProblemDescriptions(ProblemDescription[] problems) {
        this.problems = problems;
    }

    public ProblemDescription[] compareProblemDescriptions(ProblemDescription[] newProblemDescriptions) {

        ProblemDescription[] resultProblems = new ProblemDescription[newProblemDescriptions.length];

        boolean areEqual = problems.length == newProblemDescriptions.length;

        for (int i = 0; i < newProblemDescriptions.length; i++) {

            ProblemDescription newP = newProblemDescriptions[i];
            ProblemDescription resP = new ProblemDescription();

            if (newP.id != -1) {
                ProblemDescription oldP = null;
                for (int j = 0; j < problems.length; j++) {
                    ProblemDescription problem = problems[j];
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
                }
                else resP.name = null;

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
            }
            else{
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
}