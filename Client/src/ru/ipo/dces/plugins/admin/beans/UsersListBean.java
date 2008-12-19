package ru.ipo.dces.plugins.admin.beans;

import ru.ipo.dces.clientservercommunication.UserDescription;

public class UsersListBean {
    private UserDescription description;

    public UsersListBean(UserDescription description) {
        this.description = description;
    }

    public UserDescription getDescription() {
        return description;
    }

    public void setDescription(UserDescription description) {
        this.description = description;
    }

    public String toString() {
        return description.login + " : " + description.userType;
    }
}
