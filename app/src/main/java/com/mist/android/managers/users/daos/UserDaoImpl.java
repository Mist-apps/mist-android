package com.mist.android.managers.users.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mist.android.AbstractDao;
import com.mist.android.login.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gerard on 25/06/14.
 */
@Singleton
public class UserDaoImpl extends AbstractDao implements UserDao {

    private String[] ALL_COLUMNS = {
            NAME_COLUMN_USER_ID,
            NAME_COLUMN_USER_FIRSTNAME,
            NAME_COLUMN_USER_LASTNAME,
            NAME_COLUMN_USER_MAIL,
            NAME_COLUMN_USER_LOGIN
    };

    @Inject
    public UserDaoImpl(Context context) {
        super(context);
    }

    @Override
    public int save(User user) {
        if (user == null) {
            return -1;
        }
        try {
            open();
            final ContentValues cvUser = new ContentValues();
            cvUser.put(NAME_COLUMN_USER_ID, user._id);
            cvUser.put(NAME_COLUMN_USER_FIRSTNAME, user.firstName);
            cvUser.put(NAME_COLUMN_USER_LASTNAME, user.lastName);
            cvUser.put(NAME_COLUMN_USER_MAIL, user.mail);
            cvUser.put(NAME_COLUMN_USER_LOGIN, user.login);
            return (int) mDataBase.insert(DATABASE_TABLE_USER, null, cvUser);
        } finally {
            close();
        }
    }

    @Override
    public int remove() {
        try {
            open();
            return mDataBase.delete(DATABASE_TABLE_USER, null, null);
        } finally {
            close();
        }
    }

    @Override
    public User get() {
        try {
            open();
            final Cursor c = mDataBase.query(DATABASE_TABLE_USER, ALL_COLUMNS, null, null, null, null, null);
            final List<User> res = getUsersFromCursor(c);
            return res.isEmpty() ? null : res.get(0);
        } finally {
            close();
        }
    }

    @Override
    public User get(String login) {
        if (login == null || login.isEmpty()) {
            return null;
        }
        try {
            open();
            final Cursor c = mDataBase.query(DATABASE_TABLE_USER, ALL_COLUMNS, NAME_COLUMN_USER_LOGIN + " = ?", new String[]{login}, null, null, null);
            final List<User> res = getUsersFromCursor(c);
            return res.isEmpty() ? null : res.get(0);
        } finally {
            close();
        }
    }

    public static List<User> getUsersFromCursor(final Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            return new ArrayList<User>();
        }
        final List<User> tokens = new ArrayList<User>();
        if (cursor.moveToFirst()) {
            do {
                tokens.add(getUserFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        return tokens;
    }

    public static User getUserFromCursor(final Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }
        final String id = cursor.getString(cursor.getColumnIndex(NAME_COLUMN_USER_ID));
        final String firstname = cursor.getString(cursor.getColumnIndex(NAME_COLUMN_USER_FIRSTNAME));
        final String lastname = cursor.getString(cursor.getColumnIndex(NAME_COLUMN_USER_LASTNAME));
        final String mail = cursor.getString(cursor.getColumnIndex(NAME_COLUMN_USER_MAIL));
        final String login = cursor.getString(cursor.getColumnIndex(NAME_COLUMN_USER_LOGIN));
        return new User(id, firstname, lastname, mail, login);
    }
}
