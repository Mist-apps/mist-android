package com.mist.android;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gerard on 25/06/14.
 */
public abstract class AbstractDao {
    protected static final String DATABASE_NAME = "mist.db";
    protected static final int DATABASE_VERSION = 1;

    // Tables.
    protected static final String DATABASE_TABLE_TOKEN = "token";
    protected static final String DATABASE_TABLE_USER = "user";

    // Column of token table.
    protected static final String NAME_COLUMN_PRIM_TOKEN_ID = "prim_token_id";
    protected static final String NAME_COLUMN_TOKEN = "token_token";
    protected static final String NAME_COLUMN_TOKEN_EXPIRES = "token_expires";
    protected static final String NAME_COLUMN_TOKEN_USER = "user_id";

    // Column of user table.
    protected static final String NAME_COLUMN_USER_ID = "user_id";
    protected static final String NAME_COLUMN_USER_FIRSTNAME = "user_firstname";
    protected static final String NAME_COLUMN_USER_LASTNAME = "user_lastname";
    protected static final String NAME_COLUMN_USER_MAIL = "user_mail";
    protected static final String NAME_COLUMN_USER_LOGIN = "user_login";

    protected final DatabaseHelperDrug mDatabaseHelper;
    protected SQLiteDatabase mDataBase;

    public AbstractDao(Context context) {
        this.mDatabaseHelper = new DatabaseHelperDrug(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open() {
        try {
            this.mDataBase = this.mDatabaseHelper.getWritableDatabase();
        } catch (SQLException e) {
            this.mDataBase = this.mDatabaseHelper.getReadableDatabase();
        }
    }

    public void close() {
        this.mDataBase.close();
    }

    private static class DatabaseHelperDrug extends SQLiteOpenHelper {

        private static final String DATABASE_CREATE_TOKEN = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_TOKEN + " ( "
                + NAME_COLUMN_PRIM_TOKEN_ID + " integer primary key autoincrement, "
                + NAME_COLUMN_TOKEN + " text not null unique, "
                + NAME_COLUMN_TOKEN_EXPIRES + " integer not null,"
                + NAME_COLUMN_TOKEN_USER + " text not null,"
                + " FOREIGN KEY (" + NAME_COLUMN_TOKEN_USER + ") REFERENCES " + DATABASE_TABLE_USER + " (" + NAME_COLUMN_USER_ID + "));";

        private static final String DATABASE_CREATE_USER = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_USER + " ( "
                + NAME_COLUMN_USER_ID + " text primary key, "
                + NAME_COLUMN_USER_FIRSTNAME + " text not null, "
                + NAME_COLUMN_USER_LASTNAME + " text not null, "
                + NAME_COLUMN_USER_MAIL + " text not null, "
                + NAME_COLUMN_USER_LOGIN + " text not null);";

        private static final String DATABASE_DROP_TOKEN = "DROP TABLE IF EXISTS " + DATABASE_TABLE_TOKEN;
        private static final String DATABASE_DROP_USER = "DROP TABLE IF EXISTS " + DATABASE_TABLE_USER;

        public DatabaseHelperDrug(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE_TOKEN);
            db.execSQL(DATABASE_CREATE_USER);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DATABASE_DROP_TOKEN);
            db.execSQL(DATABASE_DROP_USER);
            this.onCreate(db);
        }
    }
}
