package com.mist.android.login;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gerard on 23/06/14.
 */
public class Token implements Parcelable {
    public static final Parcelable.Creator<Token> CREATOR = new Parcelable.Creator<Token>() {
        public Token createFromParcel(Parcel in) {
            return new Token(in);
        }

        public Token[] newArray(int size) {
            return new Token[size];
        }
    };

    public final String token;
    public final long expires;
    private User user;

    public Token(String token, long expires) {
        this.token = token;
        this.expires = expires;
    }

    public Token(String token, long expires, User user) {
        this.token = token;
        this.expires = expires;
        this.user = user;
    }

    public Token(Parcel in) {
        token = in.readString();
        expires = in.readLong();
        user = in.readParcelable(User.class.getClassLoader());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isValid() {
        return System.currentTimeMillis() < expires;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(token);
        dest.writeLong(expires);
        dest.writeParcelable(user, flags);
    }

    @Override
    public String toString() {
        return "Token{" +
                "token='" + token + '\'' +
                ", expires=" + expires +
                ", user=" + user +
                '}';
    }
}
