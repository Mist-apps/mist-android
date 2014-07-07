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
public class NoteDaoImpl extends AbstractDao implements NoteDao {

    private String[] ALL_COLUMNS_NOTE = {
            NAME_COLUMN_NOTE_ID,
            NAME_COLUMN_NOTE_REVISION,
            NAME_COLUMN_NOTE_USER,
            NAME_COLUMN_NOTE_TITLE,
            NAME_COLUMN_NOTE_CREATION,
            NAME_COLUMN_NOTE_MODIFICATION
    };

    @Inject
    TaskDao mTaskDao;
    @Inject
    ContentDao mContentDao;

    @Inject
    public NoteDaoImpl(Context context) {
        super(context);
    }

    @Override
    public int save(Note note) {
        if (note == null || contains(note._id)) {
            return -1;
        }
        try {
            open();
            // Save all tasks if necessary.
            mTaskDao.save(note);
            // Save content if necessary.
            mContentDao.save(note);
            // Save metadata of the note.
            ContentValues params = new ContentValues();
            params.put(NAME_COLUMN_NOTE_ID, note._id);
            params.put(NAME_COLUMN_NOTE_REVISION, note._revision);
            params.put(NAME_COLUMN_NOTE_USER, note._user);
            params.put(NAME_COLUMN_NOTE_TITLE, note.title);
            params.put(NAME_COLUMN_NOTE_CREATION, note.creationDate);
            params.put(NAME_COLUMN_NOTE_MODIFICATION, note.modificationDate);
            return (int) mDataBase.insert(DATABASE_TABLE_NOTE, null, params);
        } finally {
            close();
        }
    }

    @Override
    public int update(String identifier, Note note) {
        if (identifier == null || identifier.isEmpty()) {
            return -1;
        }
        if (note == null || !contains(note._id)) {
            return -1;
        }
        try {
            open();
            // Update all tasks if necessary.
            mTaskDao.update(identifier, note);
            // Update content if necessary.
            mContentDao.update(identifier, note);
            // Update metadata of the note.
            ContentValues params = new ContentValues();
            params.put(NAME_COLUMN_NOTE_ID, note._id);
            params.put(NAME_COLUMN_NOTE_REVISION, note._revision);
            params.put(NAME_COLUMN_NOTE_USER, note._user);
            params.put(NAME_COLUMN_NOTE_TITLE, note.title);
            params.put(NAME_COLUMN_NOTE_CREATION, note.creationDate);
            params.put(NAME_COLUMN_NOTE_MODIFICATION, note.modificationDate);
            return mDataBase.update(DATABASE_TABLE_NOTE, params, NAME_COLUMN_NOTE_ID + " = ?", new String[]{identifier});
        } finally {
            close();
        }
    }

    @Override
    public int remove(String identifier) {
        if (identifier == null || identifier.isEmpty()) {
            return -1;
        }
        try {
            open();
            // Remove all tasks for the note given.
            mTaskDao.remove(identifier);
            // Remove content for the note given.
            mContentDao.remove(identifier);
            // Remove note.
            return mDataBase.delete(DATABASE_TABLE_NOTE, NAME_COLUMN_NOTE_ID + " = ?", new String[]{identifier});
        } finally {
            close();
        }
    }

    @Override
    public Note get(String identifier) {
        if (identifier == null || identifier.isEmpty()) {
            return null;
        }
        try {
            open();
            final Cursor cNotes = mDataBase.query(DATABASE_TABLE_NOTE, ALL_COLUMNS_NOTE, NAME_COLUMN_NOTE_ID + " = ?", new String[]{identifier}, null, null, null);
            final List<Note> notesFromCursor = getNotesFromCursor(cNotes);
            if (notesFromCursor.size() == 0) {
                return null;
            }
            return notesFromCursor.get(0);
        } finally {
            close();
        }
    }

    @Override
    public List<Note> getAll() {
        try {
            open();
            final Cursor cNotes = mDataBase.query(DATABASE_TABLE_NOTE, ALL_COLUMNS_NOTE, null, null, null, null, null);
            final List<Note> notesFromCursor = getNotesFromCursor(cNotes);
            for (Note note : notesFromCursor) {
                note.setTasks(mTaskDao.getAll(note));
                if (!note.hasTasks()) {
                    note.setContent(mContentDao.get(note));
                }
            }
            return notesFromCursor;
        } finally {
            close();
        }
    }

    @Override
    public boolean contains(String identifier) {
        if (identifier == null || identifier.isEmpty()) {
            return false;
        }
        try {
            open();
            final Cursor cNotes = mDataBase.query(DATABASE_TABLE_NOTE, ALL_COLUMNS_NOTE, NAME_COLUMN_NOTE_ID + " = ?", new String[]{identifier}, null, null, null);
            final List<Note> notesFromCursor = getNotesFromCursor(cNotes);
            return notesFromCursor.size() != 0;
        } finally {
            close();
        }
    }

    public static List<Note> getNotesFromCursor(final Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            return new ArrayList<Note>();
        }
        final List<Note> notes = new ArrayList<Note>();
        if (cursor.moveToFirst()) {
            do {
                notes.add(getNoteFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        return notes;
    }

    public static Note getNoteFromCursor(final Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }
        final String id = cursor.getString(cursor.getColumnIndex(NAME_COLUMN_NOTE_ID));
        final String revision = cursor.getString(cursor.getColumnIndex(NAME_COLUMN_NOTE_REVISION));
        final String user = cursor.getString(cursor.getColumnIndex(NAME_COLUMN_NOTE_USER));
        final String title = cursor.getString(cursor.getColumnIndex(NAME_COLUMN_NOTE_TITLE));
        final long creation = cursor.getLong(cursor.getColumnIndex(NAME_COLUMN_NOTE_CREATION));
        final long modification = cursor.getLong(cursor.getColumnIndex(NAME_COLUMN_NOTE_MODIFICATION));
        return new Note(id, revision, user, title, creation, modification);
    }
}
