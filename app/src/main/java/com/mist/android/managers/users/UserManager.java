package com.mist.android.managers.users;

import com.mist.android.globals.ActionDelegate;
import com.mist.android.login.Token;

/**
 * Created by gerard on 24/06/14.
 */
public interface UserManager {
    /**
     * Log in the user to the system and save his information in cache and in database.
     *
     * @param delegate Delegate to inform the result.
     * @param login    Login of the user.
     * @param password Password of the user.
     */
    void login(ActionDelegate<Token> delegate, String login, String password);
}
