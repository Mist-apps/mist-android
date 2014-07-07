package com.mist.android.managers.notes;

import com.mist.android.globals.ActionDelegate;
import com.mist.android.main.Note;

import java.util.List;

/**
 * Created by gerard on 25/06/14.
 */
public interface NoteManager {

    /**
     * Gets all notes of the current user.
     *
     * @param delegate Delegate to inform the result.
     */
    void getAll(ActionDelegate<List<Note>> delegate);

    /**
     * Gets all notes of the current user.
     *
     * @param delegate Delegate to inform the result.
     * @param force    Force the refreshing.
     */
    void getAll(ActionDelegate<List<Note>> delegate, boolean force);

}
