package ru.ipo.dces.plugins.admin.beans;

import ru.ipo.dces.clientservercommunication.ProblemDescription;

/**
 * Created by IntelliJ IDEA.
* User: admin
* Date: 19.12.2008
* Time: 20:23:44
* To change this template use File | Settings | File Templates.
*/
public class ProblemsBean {
    private ProblemDescription description;

    public ProblemsBean(ProblemDescription description) {
        this.description = description;
    }

    public ProblemDescription getDescription() {
        return description;
    }

    public void setDescription(ProblemDescription description) {
        this.description = description;
    }

    public String toString() {
        return description == null ? "DCES error" : description.name;
    }
}
