package com.mist.android.managers.users.daos;

import com.mist.android.Dao;
import com.mist.android.login.User;

/**
 * Created by gerard on 25/06/14.
 */
public interface UserDao extends Dao {

    /**
     * Save user in the local database.
     *
     * @param user User to save.
     * @return identifier of the user saved.
     */
    int save(final User user);

    /**
     * Remove user in the local database.
     *
     * @return rows affected.
     */
    int remove();

    /**
     * Gets the first user in the database.
     *
     * @return {@link com.mist.android.login.User} object.
     */
    User get();

    /**
     * Gets a user according to his login.
     *
     * @param login Login of the user.
     * @return {@link com.mist.android.login.User} object.
     */
    User get(String login);

}
