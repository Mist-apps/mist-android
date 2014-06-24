package com.mist.android.login;

/**
 * Created by gerard on 23/06/14.
 */
public class Token {
    public final String token;
    public final long expires;
    public final User user;

    public Token(String token, long expires, User user) {
        this.token = token;
        this.expires = expires;
        this.user = user;
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
