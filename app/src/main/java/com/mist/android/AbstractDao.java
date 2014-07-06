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
    protected static final int DATABASE_VERSION = 3;

    // Tables.
    protected static final String DATABASE_TABLE_TOKEN = "tokens";
    protected static final String DATABASE_TABLE_USER = "users";
    protected static final String DATABASE_TABLE_NOTE = "notes";
    protected static final String DATABASE_TABLE_TASKS = "tasks";
    protected static final String DATABASE_TABLE_CONTENT = "contents";

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

    // Column of note table.
    protected static final String NAME_COLUMN_NOTE_ID = "note_id";
    protected static final String NAME_COLUMN_NOTE_REVISION = "note_revision";
    protected static final String NAME_COLUMN_NOTE_USER = "user_id";
    protected static final String NAME_COLUMN_NOTE_TITLE = "note_title";
    protected static final String NAME_COLUMN_NOTE_CREATION = "note_creation";
    protected static final String NAME_COLUMN_NOTE_MODIFICATION = "note_modification";

    // Column of task table.
    protected static final String NAME_COLUMN_PRIM_TASK_ID = "task_id";
    protected static final String NAME_COLUMN_TASK_ORDER = "task_order";
    protected static final String NAME_COLUMN_TASK_CONTENT = "task_content";
    protected static final String NAME_COLUMN_TASK_DONE = "task_done";
    protected static final String NAME_COLUMN_TASK_NOTE = "note_id";

    // Column of content table.
    protected static final String NAME_COLUMN_PRIM_CONTENT_ID = "content_id";
    protected static final String NAME_COLUMN_CONTENT_CONTENT = "content_content";
    protected static final String NAME_COLUMN_CONTENT_NOTE = "note_id";

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

        private static final String DATABASE_CREATE_NOTE = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_NOTE + " ( "
                + NAME_COLUMN_NOTE_ID + " text primary key, "
                + NAME_COLUMN_NOTE_REVISION + " text not null, "
                + NAME_COLUMN_NOTE_USER + " text not null, "
                + NAME_COLUMN_NOTE_TITLE + " text not null, "
                + NAME_COLUMN_NOTE_CREATION + " integer not null, "
                + NAME_COLUMN_NOTE_MODIFICATION + " integer not null, "
                + "FOREIGN KEY (" + NAME_COLUMN_NOTE_USER + ") REFERENCES " + DATABASE_TABLE_USER + " (" + NAME_COLUMN_USER_ID + ") "
                + ");";

        private static final String DATABASE_CREATE_TASK = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_TASKS + " ( "
                + NAME_COLUMN_PRIM_TASK_ID + " integer primary key autoincrement, "
                + NAME_COLUMN_TASK_ORDER + " integer not null, "
                + NAME_COLUMN_TASK_CONTENT + " text not null, "
                + NAME_COLUMN_TASK_DONE + " integer not null, "
                + NAME_COLUMN_TASK_NOTE + " text not null, "
                + "FOREIGN KEY (" + NAME_COLUMN_TASK_NOTE + ") REFERENCES " + DATABASE_TABLE_NOTE + " (" + NAME_COLUMN_NOTE_ID + ")"
                + ");";

        private static final String DATABASE_CREATE_CONTENT = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_CONTENT + " ( "
                + NAME_COLUMN_PRIM_CONTENT_ID + " integer primary key autoincrement, "
                + NAME_COLUMN_CONTENT_CONTENT + " text not null, "
                + NAME_COLUMN_CONTENT_NOTE + " text not null, "
                + "FOREIGN KEY (" + NAME_COLUMN_CONTENT_NOTE + ") REFERENCES " + DATABASE_TABLE_NOTE + " (" + NAME_COLUMN_NOTE_ID + ")"
                + ");";

        private static final String DATABASE_DROP_TOKEN = "DROP TABLE IF EXISTS " + DATABASE_TABLE_TOKEN;
        private static final String DATABASE_DROP_USER = "DROP TABLE IF EXISTS " + DATABASE_TABLE_USER;
        private static final String DATABASE_DROP_NOTE = "DROP TABLE IF EXISTS " + DATABASE_TABLE_NOTE;
        private static final String DATABASE_DROP_TASK = "DROP TABLE IF EXISTS " + DATABASE_TABLE_TASKS;
        private static final String DATABASE_DROP_CONTENT = "DROP TABLE IF EXISTS " + DATABASE_TABLE_CONTENT;

        public DatabaseHelperDrug(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE_TOKEN);
            db.execSQL(DATABASE_CREATE_USER);
            db.execSQL(DATABASE_CREATE_NOTE);
            db.execSQL(DATABASE_CREATE_TASK);
            db.execSQL(DATABASE_CREATE_CONTENT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DATABASE_DROP_TOKEN);
            db.execSQL(DATABASE_DROP_USER);
            db.execSQL(DATABASE_DROP_NOTE);
            db.execSQL(DATABASE_DROP_TASK);
            db.execSQL(DATABASE_DROP_CONTENT);
            this.onCreate(db);
        }
    }
}
