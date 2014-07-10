package com.mist.android.managers.users;

import com.mist.android.globals.ActionDelegate;
import com.mist.android.login.Token;
import com.mist.android.login.User;

/**
 * Created by gerard on 24/06/14.
 */
public interface UserManager {
    /**
     * Gets the current user in cache or local database.
     *
     * @return {@link com.mist.android.login.User} object.
     */
    User getCurrentUser();

    /**
     * Gets the current token in cache or local database.
     *
     * @return {@link com.mist.android.login.Token} object.
     */
    Token getCurrentToken();

    /**
     * Log in the user to the system and save his information in cache and in database.
     *
     * @param delegate Delegate to inform the result.
     * @param login    Login of the user.
     * @param password Password of the user.
     */
    void login(ActionDelegate<Token> delegate, String login, String password);

    /**
     * Log out the current user.
     */
    void logout();
}
