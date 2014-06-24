package com.mist.android.login;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gerard on 23/06/14.
 */
public class User implements Parcelable {
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public final String _id;
    public final String firstName;
    public final String lastName;
    public final String mail;
    public final String login;

    public User(String _id, String firstName, String lastName, String mail, String login) {
        this._id = _id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.login = login;
    }

    public User(Parcel in) {
        _id = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        mail = in.readString();
        login = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(mail);
        dest.writeString(login);
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
