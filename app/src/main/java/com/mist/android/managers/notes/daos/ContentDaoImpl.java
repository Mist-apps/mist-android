package com.mist.android.managers.notes.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mist.android.AbstractDao;
import com.mist.android.main.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gerard on 7/07/14.
 */
@Singleton
public class ContentDaoImpl extends AbstractDao implements ContentDao {

    private String[] ALL_COLUMNS_CONTENT = {
            NAME_COLUMN_PRIM_CONTENT_ID,
            NAME_COLUMN_CONTENT_CONTENT,
            NAME_COLUMN_CONTENT_NOTE
    };

    @Inject
    public ContentDaoImpl(Context context) {
        super(context);
    }

    @Override
    public int save(Note note) {
        if (note == null) {
            return -1;
        }
        try {
            open();
            if (note.getContent() != null && !note.getContent().isEmpty()) {
                ContentValues params = new ContentValues();
                params.put(NAME_COLUMN_CONTENT_CONTENT, note.getContent());
                params.put(NAME_COLUMN_CONTENT_NOTE, note._id);
                return (int) mDataBase.insert(DATABASE_TABLE_CONTENT, null, params);
            }
            return -1;
        } finally {
            close();
        }
    }

    @Override
    public String get(Note note) {
        if (note == null) {
            return null;
        }
        try {
            open();
            final Cursor cContent = mDataBase.query(DATABASE_TABLE_CONTENT, ALL_COLUMNS_CONTENT, NAME_COLUMN_CONTENT_NOTE + " = ?", new String[]{note._id}, null, null, null);
            final List<String> contentsFromCursor = getContentsFromCursor(cContent);
            if (contentsFromCursor.size() != 0) {
                return contentsFromCursor.get(0);
            }
            return null;
        } finally {
            close();
        }
    }

    public static List<String> getContentsFromCursor(final Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            return new ArrayList<String>();
        }
        final List<String> tasks = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                tasks.add(getContentFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        return tasks;
    }

    public static String getContentFromCursor(final Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }
        final String content = cursor.getString(cursor.getColumnIndex(NAME_COLUMN_CONTENT_CONTENT));
        return content;
    }
}
