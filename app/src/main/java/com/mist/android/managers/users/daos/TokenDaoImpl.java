package com.mist.android.managers.users.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mist.android.AbstractDao;
import com.mist.android.login.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gerard on 25/06/14.
 */
@Singleton
public class TokenDaoImpl extends AbstractDao implements TokenDao {

    private String[] ALL_COLUMNS = {
            NAME_COLUMN_PRIM_TOKEN_ID,
            NAME_COLUMN_TOKEN,
            NAME_COLUMN_TOKEN_EXPIRES,
            NAME_COLUMN_TOKEN_USER
    };

    @Inject
    public TokenDaoImpl(Context context) {
        super(context);
    }

    @Override
    public int save(Token token) {
        if (token == null) {
            return -1;
        }
        try {
            open();
            final ContentValues cvToken = new ContentValues();
            cvToken.put(NAME_COLUMN_TOKEN, token.token);
            cvToken.put(NAME_COLUMN_TOKEN_EXPIRES, token.expires);
            cvToken.put(NAME_COLUMN_TOKEN_USER, token.getUser()._id);
            return (int) mDataBase.insert(DATABASE_TABLE_TOKEN, null, cvToken);
        } finally {
            close();
        }
    }

    @Override
    public Token get(String userIdentifier) {
        if (userIdentifier == null || userIdentifier.isEmpty()) {
            return null;
        }
        try {
            open();
            final Cursor c = mDataBase.query(DATABASE_TABLE_TOKEN, ALL_COLUMNS, NAME_COLUMN_TOKEN_USER + " = ?", new String[]{userIdentifier}, null, null, null);
            final List<Token> res = getTokensFromCursor(c);
            return res.isEmpty() ? null : res.get(0);
        } finally {
            close();
        }
    }

    public static List<Token> getTokensFromCursor(final Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            return new ArrayList<Token>();
        }
        final List<Token> tokens = new ArrayList<Token>();
        if (cursor.moveToFirst()) {
            do {
                tokens.add(getTokenFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        return tokens;
    }

    public static Token getTokenFromCursor(final Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }
        final String token = cursor.getString(cursor.getColumnIndex(NAME_COLUMN_TOKEN));
        final long expires = cursor.getLong(cursor.getColumnIndex(NAME_COLUMN_TOKEN_EXPIRES));
        return new Token(token, expires);
    }
}
