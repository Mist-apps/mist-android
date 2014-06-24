package com.mist.android.managers.users.daos;

import com.mist.android.Dao;
import com.mist.android.login.Token;

/**
 * Created by gerard on 25/06/14.
 */
public interface TokenDao extends Dao {

    /**
     * Save token in the local database.
     *
     * @param token Token to save.
     * @return identifier of the token saved.
     */
    int save(final Token token);

    /**
     * Gets the token of a user.
     *
     * @param userIdentifier Identifier of a user.
     * @return {@link com.mist.android.login.Token} object of the user.
     */
    Token get(String userIdentifier);
}
