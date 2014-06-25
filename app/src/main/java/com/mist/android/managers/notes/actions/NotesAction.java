package com.mist.android.managers.notes.actions;

import android.os.AsyncTask;

import com.mist.android.globals.ActionDelegate;
import com.mist.android.main.Note;
import com.mist.android.managers.notes.NoteManagerImpl;

import java.util.List;

/**
 * Created by gerard on 25/06/14.
 */
public class NotesAction {

    private NoteManagerImpl.NoteInterface mNoteInterface;

    public void perform(NoteManagerImpl.NoteInterface noteInterface, ActionDelegate<List<Note>> delegate, String apiToken) {
        mNoteInterface = noteInterface;
        new NotesTask(delegate).execute(apiToken);
    }

    class NotesTask extends AsyncTask<String, Void, List<Note>> {
        private final ActionDelegate<List<Note>> mDelegate;

        NotesTask(ActionDelegate<List<Note>> delegate) {
            mDelegate = delegate;
        }

        @Override
        protected List<Note> doInBackground(String... params) {
            return mNoteInterface.getAll(params[0]);
        }

        @Override
        protected void onPostExecute(final List<Note> result) {
            if (result != null) {
                mDelegate.onSuccess(result);
            } else {
                mDelegate.onError(new Exception("Token not valid."));
            }
        }
    }
}
