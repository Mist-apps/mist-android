package com.mist.android.managers.notes;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.mist.android.globals.ActionDelegate;
import com.mist.android.main.Note;
import com.mist.android.managers.notes.actions.NotesAction;
import com.mist.android.managers.notes.daos.NoteDao;
import com.mist.android.managers.users.UserManager;
import com.mist.android.util.LogWrapper;

import java.util.List;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Header;

/**
 * Created by gerard on 25/06/14.
 */
@Singleton
public class NoteManagerImpl implements NoteManager {
    private static final String TAG = "NoteManagerImpl";
    private static final String API_URL = "http://api.leleux.me";
    /**
     * Used to make note online request.
     */
    private NoteInterface mNoteInterface;
    /**
     * Current list of notes.
     */
    private List<Note> mCurrentNotes;
    /**
     * Logger.
     */
    @Inject
    LogWrapper mLogger;
    /**
     * Dao to save notes of the user in the local database.
     */
    @Inject
    NoteDao mNoteDao;
    /**
     * User manager.
     */
    @Inject
    UserManager mUserManager;
    /**
     * Provider to get all notes of a user.
     */
    @Inject
    Provider<NotesAction> mGetNotesAction;

    public NoteManagerImpl() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();
        mNoteInterface = restAdapter.create(NoteInterface.class);
    }

    @Override
    public void getAll(ActionDelegate<List<Note>> delegate) {
        getAll(delegate, false);
    }

    @Override
    public void getAll(ActionDelegate<List<Note>> delegate, boolean force) {
        // If we would like force the update of notes, we don't make request on cache and database.
        if (!force) {
            // Check if we have notes in cache.
            if (mCurrentNotes != null && !mCurrentNotes.isEmpty()) {
                mLogger.d(TAG, "Cache hit");
                delegate.onSuccess(mCurrentNotes);
                return;
            }
            // Check if we have notes in local database.
            final List<Note> notes = mNoteDao.getAll();
            if (notes != null && !notes.isEmpty()) {
                mLogger.d(TAG, "Database hit");
                mCurrentNotes = notes;
                delegate.onSuccess(mCurrentNotes);
                return;
            }
        }
        // Otherwise, we make an online request.
        mLogger.d(TAG, "Action hit");
        mGetNotesAction.get().perform(mNoteInterface, new NotesDelegate(delegate), mUserManager.getCurrentToken().token);
    }

    /**
     * Delegate used to retrieve all notes of a user.
     */
    class NotesDelegate implements ActionDelegate<List<Note>> {

        private ActionDelegate<List<Note>> mNext;

        public NotesDelegate(ActionDelegate<List<Note>> next) {
            mNext = next;
        }

        @Override
        public void onSuccess(List<Note> result) {
            Note currentNote;
            for (Note note : result) {
                // Gets note in local database if it exists.
                currentNote = mNoteDao.get(note._id);
                if (currentNote == null) {
                    // Note isn't in local, we save it.
                    mNoteDao.save(note);
                } else if (Long.parseLong(note._revision) != Long.parseLong(currentNote._revision)) {
                    // Revision of the note on the server is superior of the note in local.
                    // So we update it.
                    mNoteDao.update(currentNote._id, note);
                }
                // Current note and note from server is equals. We can iterate to the next note.
            }
            mCurrentNotes = result;
            mNext.onSuccess(result);
        }

        @Override
        public void onError(Exception e) {
            mNext.onError(e);
        }
    }

    public interface NoteInterface {
        @GET("/note")
        List<Note> getAll(@Header("api-token") String apiToken);
    }
}
