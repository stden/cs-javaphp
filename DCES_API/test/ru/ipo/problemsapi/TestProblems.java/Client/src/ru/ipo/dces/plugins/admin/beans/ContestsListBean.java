package ru.ipo.dces.plugins.admin.beans;

import ru.ipo.dces.clientservercommunication.ContestDescription;

/**
 * Created by IntelliJ IDEA.
* User: admin
* Date: 19.12.2008
* Time: 20:03:47
* To change this template use File | Settings | File Templates.
*/
public class ContestsListBean {
    private ContestDescription description;

    public ContestsListBean(ContestDescription description) {
        this.description = description;
    }

    public ContestDescription getDescription() {
        return description;
    }

    public void setDescription(ContestDescription description) {
        this.description = description;
    }

    public String toString() {
        return description == null ? "DCES error" : description.name;
    }
}
