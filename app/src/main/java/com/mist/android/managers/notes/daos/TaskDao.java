package com.mist.android.managers.notes.daos;

import com.mist.android.Dao;
import com.mist.android.main.Note;
import com.mist.android.main.Task;

import java.util.List;

/**
 * Created by gerard on 7/07/14.
 */
public interface TaskDao extends Dao {

    /**
     * Save tasks of a note if necessary.
     *
     * @param note Note potentially concerned by tasks.
     * @return 1 if all is ok, otherwise -1;
     */
    int save(final Note note);

    /**
     * Update tasks of a note in the local database.
     *
     * @param identifier Identifier of a note.
     * @param note       Note to update.
     * @return rows affected.
     */
    int update(final String identifier, final Note note);

    /**
     * Remove tasks of a note in the local database.
     *
     * @param identifier Identifier of a note.
     * @return rows affected.
     */
    int remove(final String identifier);

    /**
     * Gets all tasks of a note.
     *
     * @param note Note potentially concerned by tasks.
     * @return List of {@link com.mist.android.main.Task}.
     */
    List<Task> getAll(final Note note);

}
