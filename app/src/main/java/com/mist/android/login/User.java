package com.mist.android.login;

/**
 * Created by gerard on 23/06/14.
 */
public class User {
    public final String _id;
    public final String firstName;
    public final String lastName;
    public final String mail;
    public final String login;

    public User(String _id) {
        this._id = _id;
        this.firstName = "";
        this.lastName = "";
        this.mail = "";
        this.login = "";
    }

    public User(String _id, String firstName, String lastName, String mail, String login) {
        this._id = _id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.login = login;
    }

    @Override
    public String toString() {
        return "User{" +
                "_id='" + _id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mail='" + mail + '\'' +
                ", login='" + login + '\'' +
                '}';
    }
}
