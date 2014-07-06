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
     * @return identifier of the note saved.
     */
    int save(final Note note);

    /**
     * Gets all notes.
     *
     * @return {@link com.mist.android.main.Note} object.
     */
    List<Note> getAll();

}
