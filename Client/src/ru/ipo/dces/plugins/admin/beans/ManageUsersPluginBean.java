package ru.ipo.dces.plugins.admin.beans;

import ru.ipo.dces.clientservercommunication.UserDescription;

public class ManageUsersPluginBean {
    private UserDescription.UserType type = UserDescription.UserType.Participant;
    private String login = "";
    private char[] password = new char[0] ;
    private String[] values = new String[0];

    public ManageUsersPluginBean() {
    }

    public ManageUsersPluginBean(ManageUsersPluginBean that) {
        this.type = that.type;
        this.login = that.login;
        this.password = that.password;

        if (that.values == null)
            this.values = null;
        else {
            values = new String[that.values.length];
            System.arraycopy(that.values, 0, values, 0, values.length);
        }
    }

    public UserDescription.UserType getType() {
        return type;
    }

    public void setType(UserDescription.UserType type) {
        this.type = type;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public void setValue(int index, String value)
    {
        if(values == null)
            return;

        values[index] = value;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

}
