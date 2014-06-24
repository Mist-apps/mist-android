package com.mist.android.managers.users.actions;

import android.os.AsyncTask;

import com.mist.android.globals.ActionDelegate;
import com.mist.android.login.Token;
import com.mist.android.managers.users.UserManagerImpl;

/**
 * Created by gerard on 25/06/14.
 */
public class LoginAction  {

    private UserManagerImpl.UserInterface mUserInterface;

    public void perform(UserManagerImpl.UserInterface userInterface, ActionDelegate<Token> delegate, String login, String password) {
        mUserInterface = userInterface;
        new UserLoginTask(delegate, login, password).execute();
    }

    class UserLoginTask extends AsyncTask<Void, Void, Token> {
        private final ActionDelegate<Token> mDelegate;
        private final String mLogin;
        private final String mPassword;

        UserLoginTask(ActionDelegate<Token> delegate, String login, String password) {
            mDelegate = delegate;
            mLogin = login;
            mPassword = password;
        }

        @Override
        protected Token doInBackground(Void... params) {
            return mUserInterface.login(mLogin, mPassword);
        }

        @Override
        protected void onPostExecute(final Token result) {
            if (result != null) {
                mDelegate.onSuccess(result);
            } else {
                mDelegate.onError(new Exception("Token not valid."));
            }
        }
    }
}
