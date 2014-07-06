package com.mist.android.managers.notes.daos;

import com.mist.android.Dao;
import com.mist.android.main.Note;

/**
 * Created by gerard on 7/07/14.
 */
public interface ContentDao extends Dao {

    /**
     * Save content of a note if necessary.
     *
     * @param note Note potentially concerned by tasks.
     * @return Identifier of the content.
     */
    int save(final Note note);

    /**
     * Gets content of a note.
     *
     * @param note Note potentially concerned by his content.
     * @return Content of the note.
     */
    String get(final Note note);

}
