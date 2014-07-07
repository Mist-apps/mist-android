package com.mist.android.managers.notes.daos;

import com.mist.android.Dao;
import com.mist.android.main.Note;

import java.util.List;

/**
 * Created by gerard on 6/07/14.
 */
public interface NoteDao extends Dao {

    /**
     * Save a note in the local database.
     *
     * @param note Note to save.
     * @return row of the note saved.
     */
    int save(final Note note);

    /**
     * Update a note in the local database.
     *
     * @param identifier Identifier of a note.
     * @param note       Note to update.
     * @return rows affected.
     */
    int update(final String identifier, final Note note);

    /**
     * Remove a note in the local database.
     *
     * @param identifier Identifier of a note.
     * @return rows affected.
     */
    int remove(final String identifier);

    /**
     * Gets a note.
     *
     * @param identifier Identifier of a note.
     * @return {@link com.mist.android.main.Note} object.
     */
    Note get(final String identifier);

    /**
     * Gets all notes.
     *
     * @return {@link com.mist.android.main.Note} object.
     */
    List<Note> getAll();

    /**
     * Check if the local database contains the note given.
     *
     * @param identifier Identifier of a note.
     * @return true if the note is in the database.
     */
    boolean contains(String identifier);
}
