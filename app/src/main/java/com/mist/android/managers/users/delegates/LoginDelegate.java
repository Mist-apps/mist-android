package com.mist.android.managers.users.delegates;

import com.mist.android.globals.ActionDelegate;
import com.mist.android.login.Token;

/**
 * Created by gerard on 25/06/14.
 */
public class LoginDelegate implements ActionDelegate<Token> {

    private ActionDelegate<Token> mNext;

    public LoginDelegate(ActionDelegate<Token> next) {
        mNext = next;
    }

    @Override
    public void onSuccess(Token result) {
        // TODO Save the token in database.
        mNext.onSuccess(result);
    }

    @Override
    public void onError(Exception e) {
        mNext.onError(e);
    }
}
