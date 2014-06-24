package com.mist.android.login;

/**
 * Created by gerard on 23/06/14.
 */
public class Token {
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
    public String toString() {
        return "Token{" +
                "token='" + token + '\'' +
                ", expires=" + expires +
                ", user=" + user +
                '}';
    }
}
